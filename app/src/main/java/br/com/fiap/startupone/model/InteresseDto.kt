package br.com.fiap.startupone.model

import java.time.LocalDateTime

data class InteresseDto(
    val idUsuario: Int,
    val idInteresse: Int,
    val nome: String,
    val periodoInicio: LocalDateTime,
    val periodoFim: LocalDateTime,
    val tempoEstimado: LocalDateTime,
    val prioridade: Int
)
