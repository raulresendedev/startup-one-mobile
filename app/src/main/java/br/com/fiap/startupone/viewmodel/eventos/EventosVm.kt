package br.com.fiap.startupone.viewmodel.eventos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.model.EventosMarcados
import br.com.fiap.startupone.service.eventos.EventosService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventosVm (
    private val userSessionManager: UserSessionManager,
    private val eventosService: EventosService
): ViewModel() {

    val eventosLiveData = MutableLiveData<List<EventosMarcados>>()
    val isLoading = MutableLiveData(false)

    private val _toastEvent = MutableLiveData<String>()
    val toastEvent: LiveData<String> get() = _toastEvent

    init {
        loadEventos()
    }

    private fun loadEventos() {
        isLoading.value = true

        val user = userSessionManager.getUserSession()
        if (user != null) {
            eventosService.buscarEventosUsuario(
                "bearer ${user.token}",
                user.idUsuario).enqueue(object :
                Callback<List<EventosMarcados>> {
                override fun onResponse(call: Call<List<EventosMarcados>>, response: Response<List<EventosMarcados>>) {
                    if (response.isSuccessful) {
                        val eventosList = response.body()
                        if (!eventosList.isNullOrEmpty()) {
                            _toastEvent.value = "Sucesso"
                            updateEventosList(eventosList)
                        } else {
                            _toastEvent.value = "Sem dados"
                        }
                    } else {
                        _toastEvent.value = "O usuario nao está autenticado"
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<List<EventosMarcados>>, t: Throwable) {
                    Log.e("API_ERROR", "Raw response: " + t)
                    isLoading.value = false
                    _toastEvent.value = "Ocorreu um erro desconhecido"
                }
            })
        } else {
            isLoading.value = false
        }
    }

    fun updateEventosList(newList: List<EventosMarcados>) {
        eventosLiveData.value = newList
    }
}
