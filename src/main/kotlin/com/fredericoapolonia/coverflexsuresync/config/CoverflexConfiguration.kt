package com.fredericoapolonia.coverflexsuresync.config

import com.fredericoapolonia.coverflexsuresync.api.CoverflexAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient

@Configuration
class CoverflexConfiguration(
    private val coverflexProperties: CoverflexProperties
) {

    @Bean
    fun coverflexAPI(): CoverflexAPI {
        val webClient = WebClient.builder()
            .baseUrl(coverflexProperties.url)
            .build()
        val adapter = WebClientAdapter.create(webClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient<CoverflexAPI>()
    }

}
