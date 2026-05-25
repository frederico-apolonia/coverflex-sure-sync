package com.fredericoapolonia.coverflexsuresync.util

import com.fredericoapolonia.coverflexsuresync.exception.CoverflexException
import com.fredericoapolonia.coverflexsuresync.exception.SureException
import org.slf4j.Logger

fun exceptionMapper(exception: Exception, logger: Logger) = when (exception) {
    is SureException -> logger.error("Error dealing with Sure: ${exception.message}")
    is CoverflexException -> logger.error("Error dealing with Coverflex: ${exception.message}")
    else -> {
        logger.error("Unexpected exception: $exception")
        throw exception
    }
}