package br.com.fiap.startupone.config

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    fun LocalDateTime.toLocalDateString(): String {
        return this.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }

    fun LocalDate.toDayMonthString(): String {
        return this.format(DateTimeFormatter.ofPattern("dd/MM"))
    }
}