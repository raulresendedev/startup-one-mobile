package br.com.fiap.startupone.viewmodel.cadastro

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.model.CadastroUsuarioDto
import br.com.fiap.startupone.model.UsuarioLogadoDto
import br.com.fiap.startupone.service.usuario.UsuarioService
import retrofit2.Call
import retrofit2.Response

class CadastroVm(
    private val userSessionManager: UserSessionManager,
    private val usuarioService: UsuarioService
) : ViewModel() {

    val nome = mutableStateOf(TextFieldValue(""))
    val email = mutableStateOf(TextFieldValue(""))
    val password = mutableStateOf("")
    val passwordConfirm = mutableStateOf("")

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    private val _toastEvent = MutableLiveData<String>()
    val toastEvent: LiveData<String> get() = _toastEvent

    fun cadastrarUsuario() {
        val cadastroUsuario = CadastroUsuarioDto(
            Nome = nome.value.text.trim(),
            email = email.value.text.trim(),
            password = password.value.trim(),
            passwordconfirm = passwordConfirm.value.trim()
        )

        val call = usuarioService.cadastrarUsuario(cadastroUsuario)

        call.enqueue(object: retrofit2.Callback<UsuarioLogadoDto> {
            override fun onResponse(
                call: Call<UsuarioLogadoDto>,
                response: Response<UsuarioLogadoDto>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { usuarioLogadoDto ->
                        userSessionManager.saveUserSession(usuarioLogadoDto)
                        Log.d("API_RESPONSE", "Raw response: ${response.body()}")
                    }
                    _navigateToHome.value = true
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                    _toastEvent.value = errorMessage
                    _navigateToHome.value = false
                }
            }

            override fun onFailure(call: Call<UsuarioLogadoDto>, t: Throwable) {
                _toastEvent.value = "Ocorreu um erro"
                _navigateToHome.value = false
            }
        })
    }

    fun resetNavigation() {
        _navigateToHome.value = false
    }
}
