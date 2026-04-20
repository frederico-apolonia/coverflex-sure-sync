package com.fredericoapolonia.coverflexsuresync.exception

open class CoverflexException(message: String): Exception(message)

class CoverflexExpiredJwtTokenException(message: String): CoverflexException(message)
class CoverflexUnauthorizedRequestException(message: String): CoverflexException(message)
class CoverflexNoMealAccountException(message: String): CoverflexException(message)
