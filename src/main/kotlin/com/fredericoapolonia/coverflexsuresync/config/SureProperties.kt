package com.fredericoapolonia.coverflexsuresync.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.UUID

@ConfigurationProperties(prefix = "sure")
class SureProperties(
    val url: String,
    val apiKey: String,
    val targetAccountName: String,
)
