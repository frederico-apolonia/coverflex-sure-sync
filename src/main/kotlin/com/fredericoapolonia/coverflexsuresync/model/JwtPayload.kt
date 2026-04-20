package com.fredericoapolonia.coverflexsuresync.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fredericoapolonia.coverflexsuresync.util.UnixTimestampDeserializer
import tools.jackson.databind.annotation.JsonDeserialize
import java.time.Instant

data class JwtPayload(
    @JsonProperty("exp")
    @JsonDeserialize(using = UnixTimestampDeserializer::class)
    val expiresAt: Instant,
    @JsonProperty("iat")
    @JsonDeserialize(using = UnixTimestampDeserializer::class)
    val issuedAt: Instant,
    @JsonProperty("jti")
    val jwtId: String? = null,
    @JsonProperty("iss")
    val issuer: String? = null,
    @JsonProperty("sub")
    val subject: String? = null,
    @JsonProperty("typ")
    val type: String? = null
) {
    fun isValid(): Boolean = Instant.now().isBefore(expiresAt)
}
