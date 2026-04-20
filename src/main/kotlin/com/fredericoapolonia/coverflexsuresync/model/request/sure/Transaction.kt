package com.fredericoapolonia.coverflexsuresync.model.request.sure

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.util.UUID

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Transaction(
    @JsonProperty("account_id")
    val accountId: UUID,
    val date: LocalDate,
    val amount: Double,
    val name: String,
    val description: String? = null,
    val notes: String? = null,
    val currency: String? = null,
    @JsonProperty("category_id")
    val categoryId: UUID? = null,
    @JsonProperty("merchant_id")
    val merchantId: UUID? = null,
    val nature: TransactionNature? = null,
    @JsonProperty("tag_ids")
    val tagIds: List<UUID>? = null
)