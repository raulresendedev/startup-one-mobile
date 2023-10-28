package br.com.fiap.startupone.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.service.usuario.UsuarioService

class LoginVmFactory(
    private val userSessionManager: UserSessionManager,
    private val usuarioService: UsuarioService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginVm::class.java)) {
            return LoginVm(userSessionManager, usuarioService) as T
        }
        throw IllegalArgumentException("Classe desconhecida")
    }
}