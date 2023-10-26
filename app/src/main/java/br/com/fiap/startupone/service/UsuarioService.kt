package br.com.fiap.startupone.service

import br.com.fiap.startupone.model.CadastroUsuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioService {
    @POST("cadastrar")
    fun cadastrarUsuario(@Body cadastroUsuario: CadastroUsuario): Call<CadastroUsuario>
}