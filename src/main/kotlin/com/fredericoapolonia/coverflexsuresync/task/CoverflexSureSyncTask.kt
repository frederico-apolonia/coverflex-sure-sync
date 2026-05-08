package com.fredericoapolonia.coverflexsuresync.task

import com.fredericoapolonia.coverflexsuresync.exception.CoverflexException
import com.fredericoapolonia.coverflexsuresync.exception.SureException
import com.fredericoapolonia.coverflexsuresync.service.CoverflexSureSyncService
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component

@Component
class CoverflexSureSyncTask(
    private val syncService: CoverflexSureSyncService
) : SchedulingConfigurer {

    private val logger = LoggerFactory.getLogger(CoverflexSureSyncTask::class.java)

    @PostConstruct
    fun runOnStartup() {
        logger.info("Starting Coverflex Sure Sync Task...")
        callSyncCoverflex()
        logger.info("Coverflex Sure Sync Task complete.")
    }

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        val trigger = CronTrigger("0 0 8 * * 1-6")
        taskRegistrar.addTriggerTask(
            { callSyncCoverflex() },
            { triggerContext ->
                val nextExecution = trigger.nextExecution(triggerContext)
                logger.info("Next Coverflex -> Sure sync scheduled at: $nextExecution")
                nextExecution
            }
        )
    }

    private fun callSyncCoverflex() = try {
        syncService.syncCoverflex()
    } catch (e: Exception) { exceptionMapper(e) }

    private fun exceptionMapper(exception: Exception) = when (exception) {
        is SureException -> logger.error("Error dealing with Sure: ${exception.message}")
        is CoverflexException -> logger.error("Error dealing with Coverflex: ${exception.message}")
        else -> {
            logger.error("Unexpected exception: $exception")
            throw exception
        }
    }
}