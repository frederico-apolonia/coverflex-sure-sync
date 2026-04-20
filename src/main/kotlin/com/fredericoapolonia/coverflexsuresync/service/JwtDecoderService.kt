package com.fredericoapolonia.coverflexsuresync.service

import com.fredericoapolonia.coverflexsuresync.config.CoverflexProperties
import com.fredericoapolonia.coverflexsuresync.model.JwtPayload
import org.springframework.stereotype.Service
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import java.util.Base64

@Service
class JwtDecoderService(
    private val coverflexProperties: CoverflexProperties
) {

    private val decodedJwtToken by lazy { decodeToken(coverflexProperties.token) }

    fun isExpired() = !decodedJwtToken.isValid()

    private fun decodeToken(token: String): JwtPayload {

        val objectMapper = JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .build()

        val payload = token.split(".")[1]
        val decodedBytes = Base64.getUrlDecoder().decode(payload)
        return JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .build()
            .readValue(decodedBytes, JwtPayload::class.java)
    }

}
