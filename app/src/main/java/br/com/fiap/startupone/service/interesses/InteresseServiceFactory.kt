package br.com.fiap.startupone.service.interesses

import br.com.fiap.startupone.config.LocalDateTimeDeserializer
import br.com.fiap.startupone.config.LocalDateTimeSerializer
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

class InteresseServiceFactory {
    companion object {
        private val URL = "http://10.0.2.2:5179/api/Interesses/"

        private val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .create()

        private val eventosFactory = Retrofit
            .Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        fun getInteressesService(): InteresseService {
            return eventosFactory.create(InteresseService::class.java)
        }
    }
}