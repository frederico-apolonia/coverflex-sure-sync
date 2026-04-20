package com.fredericoapolonia.coverflexsuresync.service

import com.fredericoapolonia.coverflexsuresync.model.request.coverflex.Movement
import com.fredericoapolonia.coverflexsuresync.model.request.sure.Transaction
import com.fredericoapolonia.coverflexsuresync.model.request.sure.TransactionNature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.Temporal

@Service
class CoverflexSureSyncService(
    private val sureService: SureService,
    private val coverflexService: CoverflexService
) {

    private val logger = LoggerFactory.getLogger(CoverflexSureSyncService::class.java)

    fun syncCoverflex() {
        val lastUploadedTransaction = sureService.getLatestTransaction()

        val startingDate = lastUploadedTransaction?.date?.plusDays(1) ?: LocalDate.MIN
        val endDate = LocalDate.now()

        val pendingTransactions = coverflexService
            .retrieveMovements(startingDate, endDate)
            .map { it.toSureTransaction() }

        logger.info("There are ${pendingTransactions.count()} pending transactions to be uploaded")
        val uploadResult = sureService.uploadAll(pendingTransactions)

        logger.info("Uploaded ${uploadResult.success} transactions, ${uploadResult.failed} failed to upload")
    }

    private fun Movement.toSureTransaction() = Transaction(
        accountId = sureService.sureAccountUuid,
        date = executedAt.atZone(ZoneOffset.UTC).toLocalDate(),
        amount = amount.amount / 100.00,
        name = merchantName ?: "NO_NAME",
        description = description,
        currency = amount.currency,
        nature = if (isDebit) {
            TransactionNature.EXPENSE
        } else {
            TransactionNature.INCOME
        }
    )

}
