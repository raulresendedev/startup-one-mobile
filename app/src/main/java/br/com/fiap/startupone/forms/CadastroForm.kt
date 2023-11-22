package br.com.fiap.startupone.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.service.usuario.UsuarioServiceFactory
import br.com.fiap.startupone.utils.showToast
import br.com.fiap.startupone.viewmodel.cadastro.CadastroVm
import br.com.fiap.startupone.viewmodel.cadastro.CadastroVmFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroForm(navController: NavHostController) {

    val context = LocalContext.current
    val userSessionManager = UserSessionManager.getInstance(context = context)
    val usuarioService = UsuarioServiceFactory.getUsuarioService()
    val viewModel: CadastroVm = viewModel(factory = CadastroVmFactory(userSessionManager, usuarioService))


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.nome.value.text,
            onValueChange = { newValue -> viewModel.nome.value = TextFieldValue(newValue) },
            label = { Text("Nome") },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.email.value.text,
            onValueChange = { newValue -> viewModel.email.value = TextFieldValue(newValue) },
            label = { Text("E-mail") },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.password.value,
            onValueChange = { newValue -> viewModel.password.value = newValue },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.passwordConfirm.value,
            onValueChange = { newValue -> viewModel.passwordConfirm.value = newValue },
            label = { Text("Confirmar Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.cadastrarUsuario()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Cadastrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        viewModel.navigateToHome.observeAsState(initial = false).value.let { shouldNavigate ->
            if (shouldNavigate) {
                navController.navigate("home") {
                    popUpTo("cadastro") { inclusive = true }
                    viewModel.resetNavigation()
                }
            }
        }

        viewModel.toastEvent.observeAsState().value?.let { message ->
            showToast(LocalContext.current, message)
        }
    }
}

