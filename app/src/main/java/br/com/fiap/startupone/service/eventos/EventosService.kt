package br.com.fiap.startupone.service.eventos

import br.com.fiap.startupone.model.EventosMarcadosDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EventosService {
    @GET("buscar-todos-por-usuario/{idUsuario}")
    fun buscarEventosUsuario(
        @Header("Authorization") token: String,
        @Path("idUsuario") idUsuario: Int,
        @Query("filtro") filtro: String
    ): Call<List<EventosMarcadosDto>>


    @POST("cadastrar")
    fun adicionarEvento(
        @Header("Authorization") token: String,
        @Body evento: EventosMarcadosDto
    ): Call<EventosMarcadosDto>

    @PATCH("atualizar-evento")
    fun atualizarEvento(
        @Header("Authorization") token: String,
        @Body eventoToEdit: EventosMarcadosDto
    ): Call<EventosMarcadosDto>

    @DELETE("deletar-evento/{idEvento}")
    fun deletarEvento(
        @Header("Authorization") token: String,
        @Path("idEvento") idEvento: Int
    ): Call<ResponseBody>

}

