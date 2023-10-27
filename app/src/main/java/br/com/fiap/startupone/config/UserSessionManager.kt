package br.com.fiap.startupone.config

import android.content.Context
import android.content.SharedPreferences
import androidx.navigation.NavHostController
import br.com.fiap.startupone.model.UsuarioLogadoDto

class UserSessionManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserSession(usuarioLogadoDto: UsuarioLogadoDto) {
        with(sharedPreferences.edit()) {
            putInt("id", usuarioLogadoDto.IdUsuario)
            putString("nome", usuarioLogadoDto.Nome)
            putString("email", usuarioLogadoDto.Email)
            apply()
        }
    }

    fun getUserSession(): UsuarioLogadoDto? {
        val id = sharedPreferences.getInt("id", -1)
        if (id == -1) return null

        val nome = sharedPreferences.getString("nome", null)
        val email = sharedPreferences.getString("email", null)

        return UsuarioLogadoDto(id, nome, email)
    }

    fun clearUserSession() {
        sharedPreferences.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getUserSession() != null
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
