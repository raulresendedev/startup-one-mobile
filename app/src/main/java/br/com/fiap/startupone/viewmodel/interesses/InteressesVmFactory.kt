package br.com.fiap.startupone.viewmodel.interesses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.service.interesses.InteresseService
import br.com.fiap.startupone.viewmodel.eventos.EventosVm

class InteressesVmFactory(
    private val userSessionManager: UserSessionManager,
    private val interesseService: InteresseService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InteressesVm::class.java)) {
            return InteressesVm(userSessionManager, interesseService) as T
        }
        throw IllegalArgumentException("Classe desconhecida")
    }
}