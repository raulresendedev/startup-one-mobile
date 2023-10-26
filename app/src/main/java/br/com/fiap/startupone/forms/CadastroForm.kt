package br.com.fiap.startupone.forms

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import br.com.fiap.startupone.model.CadastroUsuario
import br.com.fiap.startupone.service.UsuarioServiceFactory
import br.com.fiap.startupone.utils.showToast
import okhttp3.FormBody
import retrofit2.Call
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroForm(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        val context = LocalContext.current
        val nome = remember { mutableStateOf(TextFieldValue("")) }
        val email = remember { mutableStateOf(TextFieldValue("")) }
        val password = remember { mutableStateOf("") }
        val passwordConfirm = remember { mutableStateOf("") }

        OutlinedTextField(
            value = nome.value,
            onValueChange = { newValue -> nome.value = newValue },
            label = { Text("Nome") },
            singleLine = true
        )

        OutlinedTextField(
            value = email.value,
            onValueChange = { newValue -> email.value = newValue },
            label = { Text("E-mail") },
            singleLine = true
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = { newValue -> password.value = newValue },
            label = { Text("Senha") },
            singleLine = true
        )

        OutlinedTextField(
            value = passwordConfirm.value,
            onValueChange = { newValue -> passwordConfirm.value = newValue },
            label = { Text("Confirmer Senha") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val cadastroUsuario = CadastroUsuario(
                    Nome = nome.value.text,
                    email = email.value.text,
                    password = password.value,
                    passwordconfirm = passwordConfirm.value
                )

                val call = UsuarioServiceFactory.getUsuarioService().cadastrarUsuario(cadastroUsuario)

                call.enqueue(object: retrofit2.Callback<CadastroUsuario> {
                    override fun onResponse(
                        call: Call<CadastroUsuario>,
                        response: Response<CadastroUsuario>) {
                            if (response.isSuccessful){
                                Log.i("API_ERROR", "Sucesso na chamada API")
                                navController.navigate("home")
                            }else{
                                val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                                showToast(context = context, errorMessage)
                                Log.e("API_ERROR", "Falha na chamada API: $errorMessage")
                            }
                    }

                    override fun onFailure(call: Call<CadastroUsuario>, t: Throwable) {
                        showToast(context = context, "Ocorreu um erro")
                        Log.e("API_ERROR", "Falha na chamada API", t)
                    }
                })
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Cadastrar")
        }
    }
}
