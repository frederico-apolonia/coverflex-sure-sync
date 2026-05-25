package com.fredericoapolonia.coverflexsuresync.controller

import com.fredericoapolonia.coverflexsuresync.service.CoverflexSureSyncService
import com.fredericoapolonia.coverflexsuresync.util.exceptionMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/")
class SyncController(
    private val syncService: CoverflexSureSyncService
) {

    private val logger = LoggerFactory.getLogger(SyncController::class.java)

    /**
     * Triggers sync of new data from Coverflex.
     * Does not change the next scheduled time for sync.
     */
    @PostMapping("/sync")
    suspend fun sync(): ResponseEntity<Unit> {
        CoroutineScope(Dispatchers.IO).launch {
            try { syncService.syncCoverflex() }
            catch (e: Exception) { exceptionMapper(e, logger) }
        }
        return ResponseEntity.accepted().build()
    }

}
