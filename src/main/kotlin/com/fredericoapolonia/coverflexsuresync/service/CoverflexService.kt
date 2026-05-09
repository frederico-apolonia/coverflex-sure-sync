package com.fredericoapolonia.coverflexsuresync.service

import com.fredericoapolonia.coverflexsuresync.api.CoverflexAPI
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexException
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexExpiredJwtTokenException
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexNoMealAccountException
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexUnauthorizedRequestException
import com.fredericoapolonia.coverflexsuresync.model.request.coverflex.Movement
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.time.LocalDate
import java.util.UUID

private const val MEAL_ACCOUNT_NAME = "meals"

@Service
class CoverflexService(
    private val coverflexJwtService: CoverflexJwtService,
    private val coverflexAPI: CoverflexAPI
) {

    private var authenticationToken = runBlocking {  getAuthenticationToken() }
    private var bearerToken = getBearerToken()

    val mealAccountId by lazy {
        coverflexAPI
            .getPockets(bearerToken)
            .pockets.firstOrNull() { it.type == MEAL_ACCOUNT_NAME }?.let {
                UUID.fromString(it.id)
            } ?: throw CoverflexNoMealAccountException("Could not find any meal account!")
    }

    suspend fun updateToken() {
        authenticationToken = getAuthenticationToken()
        bearerToken = getBearerToken()
    }

    fun retrieveMovements(from: LocalDate, to: LocalDate): List<Movement> =
        coverflexAPI.getMovements(
            mealAccountId,
            bearerToken,
            from,
            to
        ).list

    private suspend fun getAuthenticationToken(): String {
        if (coverflexJwtService.isExpired()) {
            throw CoverflexExpiredJwtTokenException("JWT token is expired! Generate and update the current one!")
        }

        return try {
            coverflexAPI.authenticate(coverflexJwtService.getAuthenticationBodyRequest()).token
        } catch (e: Exception) {
            handeHttpException(e)
        }
    }

    private fun getBearerToken() = "Bearer $authenticationToken"

    private fun handeHttpException(e: Exception): Nothing {
        when (e) {
            is HttpClientErrorException.Unauthorized -> throw CoverflexUnauthorizedRequestException("Could not " +
                    "authenticate user on Coverflex! Check if token is still valid!")
            else -> throw CoverflexException("Error whilst processing the request: ${e.message}")
        }
    }

}
