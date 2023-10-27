package br.com.fiap.startupone.model

data class CadastroUsuarioDto (
    var Nome: String? = null,
    var email: String? = null,
    var password: String? = null,
    var passwordconfirm: String? = null
)