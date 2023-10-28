package br.com.fiap.startupone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.service.usuario.UsuarioService

class CadastroVmFactory(
    private val userSessionManager: UserSessionManager,
    private val usuarioService: UsuarioService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CadastroVm::class.java)) {
            return CadastroVm(userSessionManager, usuarioService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
