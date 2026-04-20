package com.fredericoapolonia.coverflexsuresync.model.request.sure

import com.fasterxml.jackson.annotation.JsonValue

enum class TransactionNature(
    @JsonValue val value: String
) {
    INCOME("income"),
    EXPENSE("expense"),
    INFLOW("inflow"),
    OUTFLOW("outflow"),
}
