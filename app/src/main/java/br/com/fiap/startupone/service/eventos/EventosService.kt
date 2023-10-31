package br.com.fiap.startupone.service.eventos

import br.com.fiap.startupone.model.EventosMarcadosDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface EventosService {
    @GET("buscar-todos-por-usuario/{idUsuario}")
    fun buscarEventosUsuario(
        @Header("Authorization") token: String,
        @Path("idUsuario") idUsuario: Int
    ): Call<List<EventosMarcadosDto>>

    @POST("cadastrar")
    fun adicionarEvento(
        @Header("Authorization") token: String,
        @Body evento: EventosMarcadosDto
    ): Call<EventosMarcadosDto>
}

