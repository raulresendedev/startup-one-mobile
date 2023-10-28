package br.com.fiap.startupone.forms

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.model.LoginUsuarioDto
import br.com.fiap.startupone.model.UsuarioLogadoDto
import br.com.fiap.startupone.service.usuario.UsuarioServiceFactory
import br.com.fiap.startupone.utils.showToast
import retrofit2.Call
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        val context = LocalContext.current
        val userSessionManager = UserSessionManager.getInstance(context)
        val email = remember { mutableStateOf(TextFieldValue("")) }
        val password = remember { mutableStateOf("") }

        OutlinedTextField(
            value = email.value,
            onValueChange = { newValue -> email.value = newValue },
            label = { Text("E-mail") },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { newValue -> password.value = newValue },
            label = { Text("Senha") },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {

            val loginUsuarioDto = LoginUsuarioDto(
                email = email.value.text.trim(),
                password = password.value.trim()
            )

            val call = UsuarioServiceFactory.getUsuarioService().logarUsuario(loginUsuarioDto)

            call.enqueue(object: retrofit2.Callback<UsuarioLogadoDto> {
                override fun onResponse(
                    call: Call<UsuarioLogadoDto>,
                    response: Response<UsuarioLogadoDto>
                ) {
                    if (response.isSuccessful){
                        Log.i("API_SUCCESS", "Sucesso na chamada API")

                        response.body()?.let { usuarioLogadoDto ->
                            userSessionManager.saveUserSession(usuarioLogadoDto)
                        }

                        navController.navigate("home")
                    }else{
                        val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                        showToast(context = context, errorMessage)
                        Log.e("API_ERROR", "Falha na chamada API: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<UsuarioLogadoDto>, t: Throwable) {
                    showToast(context = context, "Ocorreu um erro")
                    Log.e("API_ERROR", "Falha na chamada API", t)
                }
            })

        }) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("cadastro") },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text("Cadastre-se")
        }
    }
}
