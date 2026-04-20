package com.fredericoapolonia.coverflexsuresync.exception

open class SureException(message: String, exception: Exception? = null) : RuntimeException(message)

class SureValidationException(message: String, exception: Exception?) : SureException(message, exception)
class SureUnauthorizedException(message: String, exception: Exception?) : SureException(message, exception)
class SureNotFoundException(message: String, exception: Exception?) : SureException(message, exception)
class SureNetworkException(message: String, exception: Exception?) : SureException(message, exception)