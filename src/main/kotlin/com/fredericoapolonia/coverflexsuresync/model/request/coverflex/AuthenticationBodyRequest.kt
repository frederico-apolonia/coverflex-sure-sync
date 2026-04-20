package com.fredericoapolonia.coverflexsuresync.model.request.coverflex

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthenticationBodyRequest(
    val email: String,
    val password: String,
    @JsonProperty("user_agent_token")
    val userAgentToken: String
)