package br.com.fiap.startupone.config

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.gson.JsonElement
import java.lang.reflect.Type

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDateTime {
        return LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
