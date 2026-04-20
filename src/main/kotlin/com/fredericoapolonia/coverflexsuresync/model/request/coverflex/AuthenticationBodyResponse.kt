package com.fredericoapolonia.coverflexsuresync.model.request.coverflex

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthenticationBodyResponse(
    val token: String,
    @JsonProperty("refresh_token")
    val refreshToken: String
)