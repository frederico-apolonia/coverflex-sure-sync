package com.fredericoapolonia.coverflexsuresync.model.request.sure

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class ResponseTransaction(
    val id: UUID,
    val date: LocalDate,
    val amount: String,
    val currency: String,
    val name: String,
    val notes: String?,
    val classification: String,
    val account: Account,
    val category: Category?,
    val merchant: Merchant?,
    val tags: List<Tag>,
    val transfers: List<Transfer>?,
    @JsonProperty(value = "created_at")
    val createdAt: Instant,
    @JsonProperty(value = "updated_at")
    val updatedAt: Instant,
) {
    data class Account(
        val id: UUID,
        val name: String,
        @JsonProperty("account_type")
        val accountType: String,
    )

    data class Category(
        val id: UUID,
        val name: String,
        val classification: String?,
        val color: String,
        val icon: String
    )

    data class Merchant(
        val id: UUID,
        val name: String
    )

    data class Tag(
        val id: UUID,
        val name: String,
        val color: String
    )

    data class Transfer(
        val id: UUID,
        val amount: String,
        val currency: String,
        @JsonProperty("other_account")
        val otherAccount: Account
    )
}
