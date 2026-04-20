package com.fredericoapolonia.coverflexsuresync.model.request.sure

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class AccountDetail(
    val id: UUID,
    val name: String,
    val balance: String,
    val currency: String,
    val classification: String,
    @JsonProperty("account_type")
    val accountType: String,
)
