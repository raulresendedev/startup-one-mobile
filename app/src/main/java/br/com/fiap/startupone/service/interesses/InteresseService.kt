package br.com.fiap.startupone.service.interesses

import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.model.InteresseDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface InteresseService {

    @GET("buscar-interesses-do-usuario/{idUsuario}")
    fun buscarInteressesUsuario(
        @Header("Authorization") token: String,
        @Path("idUsuario") idUsuario: Int
    ): Call<List<InteresseDto>>

    @POST("cadastrar")
    fun adicionarInteresse(
        @Header("Authorization") token: String,
        @Body interesse: InteresseDto
    ): Call<InteresseDto>

    @PUT("atualizar-interesse")
    fun atualizarInteresse(
        @Header("Authorization") token: String,
        @Body interesseToEdit: InteresseDto
    ): Call<InteresseDto>

    @DELETE("deletar-interesse/{idInteresse}")
    fun deletarInteresse(
        @Header("Authorization") token: String,
        @Path("idInteresse") idInteresse: Int
    ): Call<ResponseBody>

}