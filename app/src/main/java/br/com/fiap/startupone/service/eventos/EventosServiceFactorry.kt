package br.com.fiap.startupone.service.eventos

import br.com.fiap.startupone.service.usuario.UsuarioService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventosServiceFactorry {
    companion object {
        private val URL = "http://10.0.2.2:5179/api/EventosMarcados/"

        private val eventosFactory = Retrofit
            .Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun getUsuarioService(): EventosService {
            return eventosFactory.create(EventosService::class.java)
        }
    }
}