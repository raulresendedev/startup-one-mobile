package br.com.fiap.startupone.viewmodel.interesses

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.model.InteresseDto
import br.com.fiap.startupone.service.interesses.InteresseService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime

class InteressesVm(
    private val userSessionManager: UserSessionManager,
    private val interesseService: InteresseService
): ViewModel() {

    val nome = mutableStateOf(TextFieldValue(""))
    val idUsuario = mutableStateOf(0)
    val idInteresse = mutableStateOf(0)
    val data = mutableStateOf(LocalDate.now())
    val inicio = mutableStateOf(LocalTime.MIDNIGHT)
    val fim = mutableStateOf(LocalTime.MIDNIGHT)
    val duracao = mutableStateOf(LocalTime.MIDNIGHT)

    val isLoading = MutableLiveData(false)

    val interessesLiveData = MutableLiveData<List<InteresseDto>>()

    val interesseAdicionado = MutableLiveData(false)

    private val _toastEvent = MutableLiveData<String?>()
    val toastEvent: MutableLiveData<String?> get() = _toastEvent

    var prioridadeSelecionada = mutableStateOf(0)
        private set

    fun prioridades(index: Int): String {
        return when (index) {
            0 -> "Baixa"
            1 -> "Média"
            2 -> "Alta"
            else -> {""}
        }
    }

    fun selecionarPrioridade(index: Int) {
        prioridadeSelecionada.value = index
    }

    fun carregarInteresses() {
        isLoading.value = true

        val user = userSessionManager.getUserSession()

        if (user != null) {
            interesseService.buscarInteressesUsuario(
                "bearer ${user.token}",
                user.idUsuario
            ).enqueue(object :
                Callback<List<InteresseDto>> {
                override fun onResponse(call: Call<List<InteresseDto>>, response: Response<List<InteresseDto>>) {
                    if (response.isSuccessful) {
                        val interessesList = response.body()
                        if (!interessesList.isNullOrEmpty()) {
                            updateInteresseList(interessesList)
                        }else{
                            clearInteresseList()
                        }
                    } else {
                        clearInteresseList()
                        _toastEvent.value = "${response.errorBody()?.string()}"
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<List<InteresseDto>>, t: Throwable) {
                    Log.e("API_ERROR", "Raw response: " + t)
                    isLoading.value = false
                    clearInteresseList()
                    _toastEvent.value = "Ocorreu um erro desconhecido"
                }
            })
        } else {
            isLoading.value = false
        }
    }

    fun salvarInteresse(){

        val user = userSessionManager.getUserSession()

        val interesse = InteresseDto(
            idUsuario = 0,
            idInteresse = 0,
            nome = nome.value.text,
            periodoInicio = data.value.atTime(inicio.value),
            periodoFim = data.value.atTime(fim.value),
            tempoEstimado = data.value.atTime(duracao.value),
            prioridade = prioridadeSelecionada.value
        )

        if (user != null) {
            interesseService.adicionarInteresse(
                "bearer ${user.token}",
                interesse).enqueue(object :
                Callback<InteresseDto> {
                override fun onResponse(
                    call: Call<InteresseDto>,
                    response: Response<InteresseDto>
                ) {
                    if (response.isSuccessful) {
                        _toastEvent.value = "Interesse cadastrado"
                        interesseAdicionado.value = true
                    } else {
                        _toastEvent.value = response.errorBody()?.string() ?: "Erro desconhecido"
                    }
                }

                override fun onFailure(call: Call<InteresseDto>, t: Throwable) {
                    Log.e("API_ERROR", "Raw response: " + t)
                    isLoading.value = false
                    _toastEvent.value = "Ocorreu um erro desconhecido"
                }
            })
        }
    }

    fun editarInteresse(){

        val user = userSessionManager.getUserSession()

        val interesse = InteresseDto(
            idUsuario = idUsuario.value,
            idInteresse = idInteresse.value,
            nome = nome.value.text,
            periodoInicio = data.value.atTime(inicio.value),
            periodoFim = data.value.atTime(fim.value),
            tempoEstimado = data.value.atTime(duracao.value),
            prioridade = prioridadeSelecionada.value
        )

        if (user != null) {
            interesseService.atualizarInteresse(
                "bearer ${user.token}",
                interesse).enqueue(object :
                Callback<InteresseDto> {
                override fun onResponse(
                    call: Call<InteresseDto>,
                    response: Response<InteresseDto>
                ) {
                    if (response.isSuccessful) {
                        _toastEvent.value = "Interesse atualizado"
                        interesseAdicionado.value = true
                    } else {
                        _toastEvent.value = response.errorBody()?.string() ?: "Erro desconhecido"
                    }
                }

                override fun onFailure(call: Call<InteresseDto>, t: Throwable) {
                    Log.e("API_ERROR", "Raw response: " + t)
                    isLoading.value = false
                    _toastEvent.value = "Ocorreu um erro desconhecido"
                }
            })
        }
    }

    fun deletarInteresse(interesse: InteresseDto){

        val user = userSessionManager.getUserSession()

        if (user != null) {
            interesseService.deletarInteresse(
                "bearer ${user.token}",
                interesse.idInteresse).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        _toastEvent.value = "Interesse Deletado"
                        val updatedList = interessesLiveData.value?.filter { it.idInteresse != interesse.idInteresse }
                        updateInteresseList(updatedList ?: listOf())
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

    fun updateInteresseList(newList: List<InteresseDto>) {
        interessesLiveData.value = newList
    }

    private fun clearInteresseList() {
        interessesLiveData.value = emptyList()
    }

    fun resetToastEvent() {
        _toastEvent.value = null
    }

    fun resetFormFields() {
        nome.value = TextFieldValue("")
        idUsuario.value = 0
        idInteresse.value = 0
        inicio.value = LocalTime.MIDNIGHT
        fim.value = LocalTime.MIDNIGHT
        duracao.value = LocalTime.MIDNIGHT
        prioridadeSelecionada.value = 0
    }

    fun setInteresse(interesseToEdit: InteresseDto) {
        idInteresse.value = interesseToEdit.idInteresse
        idUsuario.value = interesseToEdit.idUsuario
        nome.value = TextFieldValue(interesseToEdit.nome)
        inicio.value = interesseToEdit.periodoInicio.toLocalTime()
        fim.value = interesseToEdit.periodoFim.toLocalTime()
        duracao.value = interesseToEdit.tempoEstimado.toLocalTime()
        prioridadeSelecionada.value = interesseToEdit.prioridade
    }
}
