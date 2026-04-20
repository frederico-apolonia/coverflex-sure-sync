package com.fredericoapolonia.coverflexsuresync.model.request.sure

data class ListTransactionsResponse(
    val transactions: List<ResponseTransaction>,
    val pagination: Pagination
)
