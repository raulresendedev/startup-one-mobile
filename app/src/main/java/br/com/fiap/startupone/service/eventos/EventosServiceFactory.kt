package br.com.fiap.startupone.service.eventos

import br.com.fiap.startupone.config.LocalDateTimeDeserializer
import br.com.fiap.startupone.config.LocalDateTimeSerializer
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class EventosServiceFactory {
    companion object {
        private val URL = "http://10.0.2.2:5179/api/EventoMarcado/"

        private val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .create()

        private val eventosFactory = Retrofit
            .Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        fun getEventoService(): EventosService {
            return eventosFactory.create(EventosService::class.java)
        }
    }
}