package com.fredericoapolonia.coverflexsuresync.model.request.coverflex

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class MovementsBodyResponse(
    val movements: MovementList
) {
    val list: List<Movement> get() = movements.list

    data class MovementList(
        val list: List<Movement>
    )
}