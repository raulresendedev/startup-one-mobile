package br.com.fiap.startupone.viewmodel.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.model.LoginUsuarioDto
import br.com.fiap.startupone.model.UsuarioLogadoDto
import br.com.fiap.startupone.service.usuario.UsuarioService
import retrofit2.Call
import retrofit2.Response

class LoginVm (
    private val userSessionManager: UserSessionManager,
    private val usuarioService: UsuarioService
    ): ViewModel() {

    val email = mutableStateOf(TextFieldValue(""))
    val password = mutableStateOf("")

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    private val _toastEvent = MutableLiveData<String>()
    val toastEvent: LiveData<String> get() = _toastEvent

    fun logarUsuario(){
        val loginUsuarioDto = LoginUsuarioDto(
            email = email.value.text.trim(),
            password = password.value.trim()
        )

        val call = usuarioService.logarUsuario(loginUsuarioDto)

        call.enqueue(object: retrofit2.Callback<UsuarioLogadoDto> {
            override fun onResponse(
                call: Call<UsuarioLogadoDto>,
                response: Response<UsuarioLogadoDto>
            ) {
                if (response.isSuccessful){

                    response.body()?.let { usuarioLogadoDto ->
                        userSessionManager.saveUserSession(usuarioLogadoDto)
                    }

                    _navigateToHome.value = true

                }else{
                    val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                    _toastEvent.value = errorMessage
                    _navigateToHome.value = false
                }
            }

            override fun onFailure(call: Call<UsuarioLogadoDto>, t: Throwable) {
                _toastEvent.value = t.message
                _navigateToHome.value = false
            }
        })
    }
    fun resetNavigation() {
        _navigateToHome.value = false
    }
}