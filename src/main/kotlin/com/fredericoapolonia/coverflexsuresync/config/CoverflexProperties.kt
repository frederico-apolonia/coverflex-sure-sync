package com.fredericoapolonia.coverflexsuresync.config

import com.fredericoapolonia.coverflexsuresync.model.request.coverflex.AuthenticationBodyRequest
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "coverflex")
data class CoverflexProperties(
    val url: String,
    val token: String,
    val email: String,
    val password: String,
) {
    fun toAuthenticationBodyRequest() = AuthenticationBodyRequest(
        email = email,
        password = password,
        userAgentToken = token
    )
}
