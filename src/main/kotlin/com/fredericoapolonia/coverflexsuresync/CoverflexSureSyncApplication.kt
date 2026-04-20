package com.fredericoapolonia.coverflexsuresync

import com.fredericoapolonia.coverflexsuresync.config.CoverflexProperties
import com.fredericoapolonia.coverflexsuresync.config.SureProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(
    SureProperties::class,
    CoverflexProperties::class,
)
class CoverflexSureSyncApplication

fun main(args: Array<String>) {
    runApplication<CoverflexSureSyncApplication>(*args)
}
