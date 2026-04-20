package com.fredericoapolonia.coverflexsuresync.api

import com.fredericoapolonia.coverflexsuresync.model.request.Headers
import com.fredericoapolonia.coverflexsuresync.model.request.sure.ListAccountsResponse
import com.fredericoapolonia.coverflexsuresync.model.request.sure.ListTransactionsResponse
import com.fredericoapolonia.coverflexsuresync.model.request.sure.ResponseTransaction
import com.fredericoapolonia.coverflexsuresync.model.request.sure.TransactionRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import java.util.UUID

@HttpExchange
interface SureAPI {
    @GetExchange(url = "accounts")
    fun listAccounts(): ListAccountsResponse

    @GetExchange(url = "transactions")
    fun listTransactions(
        @RequestParam(value = "account_id") accountId: UUID
    ): ListTransactionsResponse

    @PostExchange(url = "transactions")
    fun createTransaction(
        @RequestBody transaction: TransactionRequest
    ): ResponseTransaction

    @DeleteExchange(url = "transactions/{id}")
    fun deleteTransaction(
        @PathVariable("id") id: UUID
    ): String
}