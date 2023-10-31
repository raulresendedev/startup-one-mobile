package br.com.fiap.startupone.model
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class EventosMarcadosDto(
    val idUsuario: Int,
    val idEventoMarcado: Int,
    val inicio: LocalDateTime,
    val fim: LocalDateTime,
    val nome: String,
    val status: Boolean,
    val categoria: Int
)