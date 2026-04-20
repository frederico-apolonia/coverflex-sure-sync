package com.fredericoapolonia.coverflexsuresync.service

import com.fredericoapolonia.coverflexsuresync.config.CoverflexProperties
import com.fredericoapolonia.coverflexsuresync.api.CoverflexAPI
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexException
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexExpiredJwtTokenException
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexNoMealAccountException
import com.fredericoapolonia.coverflexsuresync.exception.CoverflexUnauthorizedRequestException
import com.fredericoapolonia.coverflexsuresync.model.request.coverflex.Movement
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.time.LocalDate
import java.util.UUID

private const val MEAL_ACCOUNT_NAME = "meals"

@Service
class CoverflexService(
    private val coverflexProperties: CoverflexProperties,
    private val jwtDecoderService: JwtDecoderService,
    private val coverflexAPI: CoverflexAPI
) {

    val authenticationToken by lazy {
        if (jwtDecoderService.isExpired()) {
            throw CoverflexExpiredJwtTokenException("JWT token is expired! Generate and update the current one!")
        }

        try {
            coverflexAPI.authenticate(
                coverflexProperties.toAuthenticationBodyRequest()
            )
        } catch (e: Exception) {
            handeHttpException(e)
        }
    }

    val bearerToken by lazy {
        "Bearer ${authenticationToken.token}"
    }

    val mealAccountId by lazy {
        coverflexAPI
            .getPockets(bearerToken)
            .pockets.firstOrNull() { it.type == MEAL_ACCOUNT_NAME }?.let {
                UUID.fromString(it.id)
            } ?: throw CoverflexNoMealAccountException("Could not find any meal account!")
    }

    fun retrieveMovements(from: LocalDate, to: LocalDate): List<Movement> =
        coverflexAPI.getMovements(
            mealAccountId,
            bearerToken,
            from,
            to
        ).list

    private fun handeHttpException(e: Exception): Nothing {
        when (e) {
            is HttpClientErrorException.Unauthorized -> throw CoverflexUnauthorizedRequestException("Could not " +
                    "authenticate user on Coverflex! Check if token is still valid!")
            else -> throw CoverflexException("Error whilst processing the request: ${e.message}")
        }
    }

}
