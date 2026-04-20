package com.fredericoapolonia.coverflexsuresync.config

import com.fredericoapolonia.coverflexsuresync.api.SureAPI
import com.fredericoapolonia.coverflexsuresync.model.request.Headers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient

@Configuration
class SureConfiguration(
    val sureProperties: SureProperties
) {

    @Bean
    fun sureApi(): SureAPI {
        val restClient = RestClient.builder()
            .baseUrl(sureProperties.url)
            .defaultHeader(Headers.API_KEY, sureProperties.apiKey)
            .build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient<SureAPI>()
    }

}
