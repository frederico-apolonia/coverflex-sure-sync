package com.fredericoapolonia.coverflexsuresync.util

import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.deser.std.StdDeserializer
import java.time.Instant

class UnixTimestampDeserializer : StdDeserializer<Instant>(Instant::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant {
        return Instant.ofEpochSecond(p.longValue)
    }
}