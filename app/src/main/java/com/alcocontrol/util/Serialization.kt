package com.alcocontrol.util

import com.alcocontrol.dateFormatter
import com.google.gson.*
import org.threeten.bp.LocalDate
import java.lang.reflect.Type

class LocalDateSerializer : JsonSerializer<LocalDate> {

    override fun serialize(src: LocalDate, type: Type, ctx: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(dateFormatter))
    }
}

class LocalDateDeserializer : JsonDeserializer<LocalDate> {

    override fun deserialize(json: JsonElement, type: Type, ctx: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString, dateFormatter)
    }
}