package com.fredericoapolonia.coverflexsuresync.task

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
        syncService.syncCoverflex()
        logger.info("Coverflex Sure Sync Task complete.")
    }

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        val trigger = CronTrigger("0 0 8 * * 1-6")
        taskRegistrar.addTriggerTask(
            { syncService.syncCoverflex() },
            { triggerContext ->
                val nextExecution = trigger.nextExecution(triggerContext)
                logger.info("Next Coverflex -> Sure sync scheduled at: $nextExecution")
                nextExecution
            }
        )
    }
}