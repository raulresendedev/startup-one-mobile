package br.com.fiap.startupone.viewmodel.eventos

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.service.eventos.EventosService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime

class EventosVm (
    private val userSessionManager: UserSessionManager,
    private val eventosService: EventosService
): ViewModel() {

    val nome = mutableStateOf(TextFieldValue(""))
    val idUsuario = mutableStateOf(0)
    val idEventoMarcado = mutableStateOf(0)
    val data = mutableStateOf(LocalDate.now())
    val inicio = mutableStateOf(LocalTime.MIDNIGHT)
    val fim = mutableStateOf(LocalTime.MIDNIGHT)

    var selectedIndex = mutableStateOf(0)
        private set

    val selectedFilter: String
        get() = when (selectedIndex.value) {
            0 -> "pendentes"
            1 -> "atrasados"
            2 -> "concluidos"
            else -> "futuros"
        }

    val eventosLiveData = MutableLiveData<List<EventosMarcadosDto>>()
    val isLoading = MutableLiveData(false)
    val eventoAdicionado = MutableLiveData(false)
    private val _toastEvent = MutableLiveData<String?>()
    val toastEvent: MutableLiveData<String?> get() = _toastEvent

    init {
        carregarEventos()
    }

    fun selecionarFiltro(index: Int) {
        selectedIndex.value = index
        carregarEventos()
    }

    private fun carregarEventos() {
        isLoading.value = true

        val user = userSessionManager.getUserSession()

        if (user != null) {
            eventosService.buscarEventosUsuario(
                "bearer ${user.token}",
                user.idUsuario,
                selectedFilter).enqueue(object :
                Callback<List<EventosMarcadosDto>> {
                override fun onResponse(call: Call<List<EventosMarcadosDto>>, response: Response<List<EventosMarcadosDto>>) {
                    if (response.isSuccessful) {
                        val eventosList = response.body()
                        if (!eventosList.isNullOrEmpty()) {
                            updateEventosList(eventosList)
                        }else{
                            clearEventosList()
                        }
                    } else {
                        clearEventosList()
                        _toastEvent.value = "${response.errorBody()?.string()}"
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<List<EventosMarcadosDto>>, t: Throwable) {
                    Log.e("API_ERROR", "Raw response: " + t)
                    isLoading.value = false
                    clearEventosList()
                    _toastEvent.value = "Ocorreu um erro desconhecido"
                }
            })
        } else {
            isLoading.value = false
        }
    }

    fun salvarEvento(){

        val user = userSessionManager.getUserSession()

        val eventoMarcado = EventosMarcadosDto(
            idUsuario = 0,
            idEventoMarcado = 0,
            nome = nome.value.text,
            inicio = data.value.atTime(inicio.value),
            fim = data.value.atTime(fim.value),
            concluido = false,
            confirmado = true,
            categoria = 0
        )

        if (user != null) {
            eventosService.adicionarEvento(
                "bearer ${user.token}",
                eventoMarcado).enqueue(object :
                Callback<EventosMarcadosDto> {
                override fun onResponse(
                    call: Call<EventosMarcadosDto>,
                    response: Response<EventosMarcadosDto>
                ) {
                    if (response.isSuccessful) {
                        _toastEvent.value = "Evento cadastrado"
                        carregarEventos()
                        eventoAdicionado.value = true
                    } else {
                        _toastEvent.value = response.errorBody()?.string() ?: "Erro desconhecido"
                    }
                }

                override fun onFailure(call: Call<EventosMarcadosDto>, t: Throwable) {
                    Log.e("API_ERROR", "Raw response: " + t)
                    isLoading.value = false
                    _toastEvent.value = "Ocorreu um erro desconhecido"
                }
            })
        }
    }

    fun editarEvento(){

        val user = userSessionManager.getUserSession()

        val eventoMarcado = EventosMarcadosDto(
            idUsuario = idUsuario.value,
            idEventoMarcado = idEventoMarcado.value,
            nome = nome.value.text,
            inicio = data.value.atTime(inicio.value),
            fim = data.value.atTime(fim.value),
            concluido = false,
            confirmado = true,
            categoria = 0
        )

        if (user != null) {
            eventosService.atualizarEvento(
                "bearer ${user.token}",
                eventoMarcado).enqueue(object :
                Callback<EventosMarcadosDto> {
                override fun onResponse(
                    call: Call<EventosMarcadosDto>,
                    response: Response<EventosMarcadosDto>
                ) {
                    if (response.isSuccessful) {
                        val updatedEvento = response.body()
                        if (updatedEvento != null) {
                            carregarEventos()
                            _toastEvent.value = "Evento editado"
                            eventoAdicionado.value = true
                        }
                    } else {
                        _toastEvent.value = response.errorBody()?.string() ?: "Erro desconhecido"
                    }
                }

                override fun onFailure(call: Call<EventosMarcadosDto>, t: Throwable) {
                    Log.e("API_ERROR", "Raw response: " + t)
                    isLoading.value = false
                    _toastEvent.value = "Ocorreu um erro desconhecido"
                }
            })
        }
    }

    fun deletarEvento(evento: EventosMarcadosDto){

        val user = userSessionManager.getUserSession()

        if (user != null) {
            eventosService.deletarEvento(
                "bearer ${user.token}",
                evento.idEventoMarcado).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        _toastEvent.value = "Evento Deletado"
                        val updatedList = eventosLiveData.value?.filter { it.idEventoMarcado != evento.idEventoMarcado }
                        updateEventosList(updatedList ?: listOf())
                    } else {
                        _toastEvent.value = response.errorBody()?.string() ?: "Erro desconhecido"
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("API_ERROR", "Raw response: " + t)
                    isLoading.value = false
                    _toastEvent.value = "Ocorreu um erro desconhecido"
                }
            })
        }
    }

    fun atualizarConclusaoEvento(evento: EventosMarcadosDto){

        val user = userSessionManager.getUserSession()

        if (user != null) {
            eventosService.atualizarConclusaoEvento(
                "bearer ${user.token}",
                evento.idEventoMarcado).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                    } else {
                        _toastEvent.value = response.errorBody()?.string() ?: "Erro desconhecido"
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("API_ERROR", "Raw response: " + t)
                    isLoading.value = false
                    _toastEvent.value = "Ocorreu um erro desconhecido"
                }
            })
        }
    }

    fun updateEventosList(newList: List<EventosMarcadosDto>) {
        eventosLiveData.value = newList
    }
    private fun clearEventosList() {
        eventosLiveData.value = emptyList()
    }
    fun resetToastEvent() {
        _toastEvent.value = null
    }

    fun loadEvent(eventoToEdit: EventosMarcadosDto) {
        idEventoMarcado.value = eventoToEdit.idEventoMarcado
        idUsuario.value = eventoToEdit.idUsuario
        nome.value = TextFieldValue(eventoToEdit.nome)
        data.value = eventoToEdit.inicio.toLocalDate()
        inicio.value = eventoToEdit.inicio.toLocalTime()
        fim.value = eventoToEdit.fim.toLocalTime()
    }
    fun resetFormFields() {
        nome.value = TextFieldValue("")
        idUsuario.value = 0
        idEventoMarcado.value = 0
        data.value = LocalDate.now()
        inicio.value = LocalTime.MIDNIGHT
        fim.value = LocalTime.MIDNIGHT
    }

}
