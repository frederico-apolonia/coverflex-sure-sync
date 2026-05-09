package com.fredericoapolonia.coverflexsuresync.service

import com.fredericoapolonia.coverflexsuresync.config.CoverflexProperties
import com.fredericoapolonia.coverflexsuresync.model.JwtPayload
import com.fredericoapolonia.coverflexsuresync.model.request.coverflex.AuthenticationBodyRequest
import org.springframework.stereotype.Service
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import java.util.Base64

@Service
class CoverflexJwtService(
    private val coverflexProperties: CoverflexProperties
) {

    private var currentToken = coverflexProperties.token
    private var decodedJwtToken = decodeToken(currentToken)

    fun isExpired() = !decodedJwtToken.isValid()

    fun updateToken(token: String) {
        currentToken = token
        decodedJwtToken = decodeToken(currentToken)
    }

    fun expiryDate() = decodedJwtToken.expiresAt

    fun getAuthenticationBodyRequest() = AuthenticationBodyRequest(
        email = coverflexProperties.email,
        password = coverflexProperties.password,
        userAgentToken = currentToken
    )

    private fun decodeToken(token: String): JwtPayload {
        val payload = token.split(".")[1]
        val decodedBytes = Base64.getUrlDecoder().decode(payload)
        return JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .build()
            .readValue(decodedBytes, JwtPayload::class.java)
    }

}
