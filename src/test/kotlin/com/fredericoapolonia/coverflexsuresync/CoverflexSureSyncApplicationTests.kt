package com.fredericoapolonia.coverflexsuresync

import com.fredericoapolonia.coverflexsuresync.model.request.sure.Transaction
import com.fredericoapolonia.coverflexsuresync.model.request.sure.TransactionNature
import com.fredericoapolonia.coverflexsuresync.model.request.sure.TransactionRequest
import com.fredericoapolonia.coverflexsuresync.api.CoverflexAPI
import com.fredericoapolonia.coverflexsuresync.api.SureAPI
import com.fredericoapolonia.coverflexsuresync.config.CoverflexProperties
import com.fredericoapolonia.coverflexsuresync.config.SureProperties
import com.fredericoapolonia.coverflexsuresync.model.request.Headers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
class CoverflexAPITest @Autowired constructor(
    private val coverflexProperties: CoverflexProperties,
    private val sureProperties: SureProperties,
) {

    private val coverflexApi by lazy {
        val restClient = RestClient.builder()
            .baseUrl(coverflexProperties.url)
            .build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        factory.createClient<CoverflexAPI>()
    }

    private val sureApi: SureAPI by lazy {
        val restClient = RestClient.builder()
            .baseUrl(sureProperties.url)
            .defaultHeader(Headers.API_KEY, sureProperties.apiKey)
            .build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        factory.createClient<SureAPI>()
    }

    @Test
    fun `authenticate returns a token`() {
        val result = coverflexApi.authenticate(coverflexProperties.toAuthenticationBodyRequest())
        println(result)
        assert(result.token.isNotEmpty())
    }

    @Test fun `getTransactions returns a list of transactions`() {
        val authResult = coverflexApi.authenticate(coverflexProperties.toAuthenticationBodyRequest())

        val pocket = coverflexApi.getPockets(
            token = "Bearer ${authResult.token}"
        ).pockets.firstOrNull() { it.type == "meals" }

        val result = coverflexApi.getMovements(
            accountId = UUID.fromString(pocket!!.id),
            token = "Bearer ${authResult.token}",
        )
        println(result.list)
    }

    @Test fun `getTransactions allows filtering by date`() {
        val authResult = coverflexApi.authenticate(coverflexProperties.toAuthenticationBodyRequest())

        val pocket = coverflexApi.getPockets(
            token = "Bearer ${authResult.token}"
        ).pockets.firstOrNull() { it.type == "meals" }

        assertNotNull(pocket)

        val result = coverflexApi.getMovements(
            accountId = UUID.fromString(pocket.id),
            token = "Bearer ${authResult.token}",
            from = LocalDate.of(2026, 3, 1),
            to = LocalDate.of(2026, 3, 31)
        )

        println(result)
    }

    @Test fun `accounts returns a list of available Sure accounts`() {
        val accountsResult = sureApi.listAccounts()

        val coverflexAccount = accountsResult.accounts.firstOrNull() {
            it.name.contains("coverflex", ignoreCase = true)
        }

        assertNotNull(coverflexAccount)

        val sureCoverflexTransactions = sureApi.listTransactions(coverflexAccount.id)
        println(sureCoverflexTransactions)
    }

    @Test fun `should be able to create a dummy transaction and then delete it`() {
        val accountsResult = sureApi.listAccounts()

        val coverflexAccount = accountsResult.accounts.firstOrNull() {
            it.name.contains("coverflex", ignoreCase = true)
        }

        assertNotNull(coverflexAccount)

        val dummyTransaction = Transaction(
            accountId = coverflexAccount.id,
            date = LocalDate.now(),
            amount = 100.0,
            name = "Test Transaction",
            nature = TransactionNature.EXPENSE
        )

        val createTransactionResponse = sureApi.createTransaction(TransactionRequest(dummyTransaction))

        assertNotNull(createTransactionResponse)
        println(createTransactionResponse)

        val transactionFromList = sureApi.listTransactions(coverflexAccount.id)
            .transactions?.firstOrNull {
                it.id == createTransactionResponse.id
            }
        assertNotNull(transactionFromList)

        sureApi.deleteTransaction(transactionFromList.id)

        sureApi.listTransactions(coverflexAccount.id)
            .transactions?.firstOrNull {
                it.id == createTransactionResponse.id
            }.also { assertNull(it) }
    }
}