package br.com.fiap.startupone.model

data class UsuarioLogadoDto(
    val idUsuario: Int,
    val nome: String?,
    val email: String?,
    val token: String?
)
