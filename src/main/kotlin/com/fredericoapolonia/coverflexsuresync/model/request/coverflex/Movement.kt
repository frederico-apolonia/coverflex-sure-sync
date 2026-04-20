package com.fredericoapolonia.coverflexsuresync.model.request.coverflex

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class Movement(
    val id: String,
    val name: String?,
    val status: String,
    val type: String,
    val description: String?,
    val category: String?,
    val amount: Amount,
    @JsonProperty("is_debit")
    val isDebit: Boolean,
    @JsonProperty("pocket_id")
    val pocketId: String,
    @JsonProperty("executed_at")
    val executedAt: Instant,
    @JsonProperty("merchant_name")
    val merchantName: String?,
    val pocket: Pocket,
    val subcategory: String?,
    @JsonProperty("balance_after")
    val balanceAfter: Amount?,
    @JsonProperty("balance_before")
    val balanceBefore: Amount?,
    @JsonProperty("is_transfer_adjustment")
    val isTransferAdjustment: Boolean
) {
    data class Pocket(
        val id: String,
        val type: String,
        val balance: Balance?,
        @JsonProperty("owner_id")
        val ownerId: String,
        @JsonProperty("provider_id")
        val providerId: String,
        @JsonProperty("owner_type")
        val ownerType: String
    )

    data class Amount(
        val currency: String,
        val amount: Int
    )

    data class Balance(
        val currency: String,
        val amount: Int
    )
}
