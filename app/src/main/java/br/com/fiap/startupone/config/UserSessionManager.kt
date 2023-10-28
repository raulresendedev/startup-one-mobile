package br.com.fiap.startupone.config

import android.content.Context
import android.content.SharedPreferences
import androidx.navigation.NavHostController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import br.com.fiap.startupone.model.UsuarioLogadoDto

class UserSessionManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_session_encrypted",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUserSession(usuarioLogadoDto: UsuarioLogadoDto) {
        with(sharedPreferences.edit()) {
            putInt("id", usuarioLogadoDto.idUsuario)
            putString("nome", usuarioLogadoDto.nome)
            putString("email", usuarioLogadoDto.email)
            apply()
        }

        with(encryptedSharedPreferences.edit()) {
            putString("token", usuarioLogadoDto.token)
            apply()
        }
    }

    fun getUserSession(): UsuarioLogadoDto? {
        val id = sharedPreferences.getInt("id", -1)

        val nome = sharedPreferences.getString("nome", null)
        val email = sharedPreferences.getString("email", null)
        val token = encryptedSharedPreferences.getString("token", null)

        return UsuarioLogadoDto(id, nome, email, token)
    }

    fun clearUserSession() {
        sharedPreferences.edit().clear().apply()
        encryptedSharedPreferences.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        val userSession = getUserSession()
        val a = userSession?.idUsuario != -1 && userSession?.token != null
        return a
    }

    fun redirecionarSemSessao(navController: NavHostController) {
        if (!isLoggedIn()) {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != "login") {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserSessionManager? = null

        fun getInstance(context: Context): UserSessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserSessionManager(context).also {
                    INSTANCE = it
                }
            }
        }
    }
}
