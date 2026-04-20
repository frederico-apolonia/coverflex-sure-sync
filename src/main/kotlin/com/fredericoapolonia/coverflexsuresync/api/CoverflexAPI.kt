package com.fredericoapolonia.coverflexsuresync.api

import com.fredericoapolonia.coverflexsuresync.model.request.Headers
import com.fredericoapolonia.coverflexsuresync.model.request.coverflex.AuthenticationBodyRequest
import com.fredericoapolonia.coverflexsuresync.model.request.coverflex.AuthenticationBodyResponse
import com.fredericoapolonia.coverflexsuresync.model.request.coverflex.MovementsBodyResponse
import com.fredericoapolonia.coverflexsuresync.model.request.sure.PocketsResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import java.time.LocalDate
import java.util.UUID

@HttpExchange
interface CoverflexAPI {

    @PostExchange(url = "employee/sessions")
    fun authenticate(
        @RequestBody authParams: AuthenticationBodyRequest
    ): AuthenticationBodyResponse

    @GetExchange(url = "employee/pockets")
    fun getPockets(
        @RequestHeader(Headers.AUTHORIZATION) token: String
    ): PocketsResponse

    @GetExchange(url = "employee/movements?pocked_id={accountId}&pagination=no")
    fun getMovements(
        @PathVariable accountId: UUID,
        @RequestHeader(Headers.AUTHORIZATION) token: String
    ): MovementsBodyResponse

    @GetExchange(url = "employee/movements?pocket_id={accountId}&filters[movement_from]={from}&filters[movement_to]={to}&filters[subcategory_not_in][]=budget_expiration&filters[subcategory_not_in][]=budget_reset&filters[subcategory_not_in][]=budget_removal&pagination=no")
    fun getMovements(
        @PathVariable accountId: UUID,
        @RequestHeader(Headers.AUTHORIZATION) token: String,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate
    ): MovementsBodyResponse

}