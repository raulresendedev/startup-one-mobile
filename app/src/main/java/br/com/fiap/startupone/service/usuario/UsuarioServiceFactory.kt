package br.com.fiap.startupone.service.usuario

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UsuarioServiceFactory {
    companion object {
        private val URL = "http://10.0.2.2:5179/api/Usuario/"

        private val usuarioFactory = Retrofit
            .Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun getUsuarioService(): UsuarioService {
            return usuarioFactory.create(UsuarioService::class.java)
        }
    }
}
