package com.fredericoapolonia.coverflexsuresync.model.request.sure

data class ListAccountsResponse(
    val accounts: List<AccountDetail>,
    val pagination: Pagination
)
