package br.com.fiap.startupone.service.usuario

import br.com.fiap.startupone.model.CadastroUsuarioDto
import br.com.fiap.startupone.model.LoginUsuarioDto
import br.com.fiap.startupone.model.UsuarioLogadoDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioService {
    @POST("cadastrar")
    fun cadastrarUsuario(@Body cadastroUsuario: CadastroUsuarioDto): Call<UsuarioLogadoDto>

    @POST("logar")
    fun logarUsuario(@Body loginUsuario: LoginUsuarioDto): Call<UsuarioLogadoDto>
}