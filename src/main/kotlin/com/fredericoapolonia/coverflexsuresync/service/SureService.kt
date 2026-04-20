package com.fredericoapolonia.coverflexsuresync.service

import com.fredericoapolonia.coverflexsuresync.api.SureAPI
import com.fredericoapolonia.coverflexsuresync.config.SureProperties
import com.fredericoapolonia.coverflexsuresync.exception.SureException
import com.fredericoapolonia.coverflexsuresync.exception.SureNetworkException
import com.fredericoapolonia.coverflexsuresync.exception.SureNotFoundException
import com.fredericoapolonia.coverflexsuresync.exception.SureUnauthorizedException
import com.fredericoapolonia.coverflexsuresync.exception.SureValidationException
import com.fredericoapolonia.coverflexsuresync.model.request.sure.ResponseTransaction
import com.fredericoapolonia.coverflexsuresync.model.request.sure.Transaction
import com.fredericoapolonia.coverflexsuresync.model.request.sure.TransactionRequest
import com.fredericoapolonia.coverflexsuresync.model.request.sure.UploadResult
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException
import java.time.LocalDate
import java.util.UUID

@Service
class SureService(
    private val sureApi: SureAPI,
    private val sureProperties: SureProperties
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    val sureAccountUuid by lazy {
        sureApi.listAccounts().accounts
            .firstOrNull { account ->
                account.name.contains(sureProperties.targetAccountName, ignoreCase = true)
            }?.id ?: throw SureException("No Coverflex account found!")
    }

    fun getLatestTransaction(): ResponseTransaction? = runCatching {
        sureApi
            .listTransactions(sureAccountUuid)
            .transactions.maxByOrNull { it.date }
    }.onFailure { e ->
        logger.error("Error fetching latest transaction", e as Exception)
    }.getOrNull()

    fun uploadAll(transactions: List<Transaction>): UploadResult {
        var success = 0
        var failed = 0

        transactions.forEach { transaction ->
            if (uploadTransaction(transaction)) success++ else failed++
        }

        return UploadResult(success = success, failed = failed)
    }

    private fun uploadTransaction(transaction: Transaction) = runCatching {
            logger.info("Uploading ${transaction.name} - ${transaction.date} - ${transaction.amount}")
            sureApi.createTransaction(transaction = TransactionRequest(transaction))
        }.onFailure { e ->
            when {
                e is HttpClientErrorException && e.statusCode == HttpStatus.UNPROCESSABLE_ENTITY -> {
                    logger.error("Validation failed for transaction ${transaction.name} on ${transaction.date}: ${e.responseBodyAsString}")
                }
                else -> handleSureHttpException(e as Exception)
            }
        }.isSuccess

    private fun handleSureHttpException(e: Exception): Nothing {
        logger.error("Error whilst processing Sure request!")
        when (e) {
            is HttpClientErrorException if e.statusCode == HttpStatus.UNPROCESSABLE_ENTITY -> {
                throw SureValidationException("Transaction validation failed: ${e.responseBodyAsString}", e)
            }

            is HttpClientErrorException if e.statusCode == HttpStatus.UNAUTHORIZED -> {
                throw SureUnauthorizedException("Invalid or missing Sure API key", e)
            }

            is HttpClientErrorException if e.statusCode == HttpStatus.NOT_FOUND -> {
                throw SureNotFoundException("Account not found", e)
            }

            is HttpClientErrorException if e.statusCode == HttpStatus.BAD_REQUEST -> {
                throw SureValidationException("Invalid request parameters: ${e.responseBodyAsString}", e)
            }

            is ResourceAccessException -> {
                throw SureNetworkException("Failed to reach Sure API", e)
            }

            else -> throw SureException("Unexpected error calling Sure API: ${e.message}", e)
        }
    }

}
