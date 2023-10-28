package br.com.fiap.startupone.model
import java.util.Date

data class EventosMarcados(
    val idEventoMarcado: Int,
    val inicio: Date,
    val fim: Date,
    val nome: String,
    val status: Boolean,
    val categoria: String
)