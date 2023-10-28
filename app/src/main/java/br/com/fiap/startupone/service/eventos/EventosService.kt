package br.com.fiap.startupone.service.eventos

import br.com.fiap.startupone.model.EventosMarcados
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EventosService {

    @GET("buscar-todos-por-usuario/{idUsuario}")
    fun buscarEventosUsuario(@Path("idUsuario") idUsuario: Int): Call<List<EventosMarcados>>
}