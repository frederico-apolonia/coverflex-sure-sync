package com.fredericoapolonia.coverflexsuresync.config

import com.fredericoapolonia.coverflexsuresync.api.CoverflexAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient

@Configuration
class CoverflexConfiguration(
    private val coverflexProperties: CoverflexProperties
) {

    @Bean
    fun coverflexAPI(): CoverflexAPI {
        val restClient = RestClient.builder()
            .baseUrl(coverflexProperties.url)
            .build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient<CoverflexAPI>()
    }

}
