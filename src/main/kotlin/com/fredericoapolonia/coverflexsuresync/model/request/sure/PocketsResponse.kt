package com.fredericoapolonia.coverflexsuresync.model.request.sure

import com.fredericoapolonia.coverflexsuresync.model.request.coverflex.Movement

data class PocketsResponse(
    val pockets: List<Movement.Pocket>
)
