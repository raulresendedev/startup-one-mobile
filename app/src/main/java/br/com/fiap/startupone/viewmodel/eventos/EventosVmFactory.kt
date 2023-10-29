package br.com.fiap.startupone.viewmodel.eventos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.service.eventos.EventosService
import br.com.fiap.startupone.viewmodel.login.LoginVm

class EventosVmFactory(
    private val userSessionManager: UserSessionManager,
    private val eventosService: EventosService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventosVm::class.java)) {
            return EventosVm(userSessionManager, eventosService) as T
        }
        throw IllegalArgumentException("Classe desconhecida")
    }
}