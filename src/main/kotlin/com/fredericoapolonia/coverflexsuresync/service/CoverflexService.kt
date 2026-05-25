package com.fredericoapolonia.coverflexsuresync.service

import com.fredericoapolonia.coverflexsuresync.api.CoverflexAPI
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexException
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexExpiredJwtTokenException
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexNoMealAccountException
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexUnauthorizedRequestException
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

    private val mealAccountId = runBlocking { retrieveMealAccountId() }

    private suspend fun retrieveBearerToken() = "Bearer ${getAuthenticationToken()}"

    suspend fun retrieveMovements(from: LocalDate, to: LocalDate) =
        coverflexAPI.getMovements(
            mealAccountId,
            retrieveBearerToken(),
            from,
            to
        ).list

    private suspend fun getAuthenticationToken(): String = if (coverflexJwtService.isExpired()) {
            throw CoverflexExpiredJwtTokenException("JWT token is expired! Generate and update the current one!")
        } else {
            try {
                coverflexAPI.authenticate(coverflexJwtService.getAuthenticationBodyRequest()).token
            } catch (e: Exception) {
                handeHttpException(e)
            }
        }

    private fun handeHttpException(e: Exception): Nothing {
        when (e) {
            is HttpClientErrorException.Unauthorized -> throw CoverflexUnauthorizedRequestException("Could not " +
                    "authenticate user on Coverflex! Check if token is still valid!")
            else -> throw CoverflexException("Error whilst processing the request: ${e.message}")
        }
    }

    private suspend fun retrieveMealAccountId() =
        coverflexAPI
            .getPockets(retrieveBearerToken())
            .pockets
            .firstOrNull() { it.type == MEAL_ACCOUNT_NAME }?.let {
                UUID.fromString(it.id)
            } ?: throw CoverflexNoMealAccountException("Could not find any meal account!")
}
