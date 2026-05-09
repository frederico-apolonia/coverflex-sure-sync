package com.fredericoapolonia.coverflexsuresync.controller

import com.fredericoapolonia.coverflexsuresync.model.request.UpdateTokenRequest
import com.fredericoapolonia.coverflexsuresync.service.CoverflexJwtService
import com.fredericoapolonia.coverflexsuresync.service.CoverflexService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class JWTController(
    private val coverflexJwtService: CoverflexJwtService,
    private val coverflexService: CoverflexService
) {

    private val logger = LoggerFactory.getLogger(JWTController::class.java)

    @PostMapping("/update")
    suspend fun updateToken(
        @RequestBody body: UpdateTokenRequest,
    ): ResponseEntity<Unit> {
        logger.info("Received update token: ${body.token.substring(0, 10)}...")

        logger.info("Updating CoverflexJWT token")
        coverflexJwtService.updateToken(body.token)

        logger.info("Triggered token update on Coverflex Service")
        coverflexService.updateToken()

        logger.info("Token updated! Expiry date: ${coverflexJwtService.expiryDate()}")
        return ResponseEntity.ok().build()
    }

    @GetMapping("/expiry")
    suspend fun getExpiryDate(): String? = "${coverflexJwtService.expiryDate()}"

}