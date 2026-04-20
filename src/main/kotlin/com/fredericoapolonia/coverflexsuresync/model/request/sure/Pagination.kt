package com.fredericoapolonia.coverflexsuresync.model.request.sure

import com.fasterxml.jackson.annotation.JsonProperty

data class Pagination(
    val page: Int,
    @JsonProperty("per_page")
    val perPage: Int,
    @JsonProperty("total_count")
    val totalCount: Int,
    @JsonProperty("total_pages")
    val totalPages: Int,
)
