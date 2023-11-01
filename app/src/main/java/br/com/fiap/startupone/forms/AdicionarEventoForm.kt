package br.com.fiap.startupone.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.startupone.components.PickDateButton
import br.com.fiap.startupone.components.PickTimeButton
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.service.eventos.EventosServiceFactory
import br.com.fiap.startupone.utils.showToast
import br.com.fiap.startupone.viewmodel.eventos.EventosVm
import br.com.fiap.startupone.viewmodel.eventos.EventosVmFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarEventoForm(onClose: () -> Unit) {

    val context = LocalContext.current
    val userSessionManager = UserSessionManager.getInstance(context = context)
    val eventoService = EventosServiceFactory.getEventoService()
    val viewModel: EventosVm = viewModel(factory = EventosVmFactory(userSessionManager, eventoService))

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = viewModel.nome.value.text,
            onValueChange = { newValue -> viewModel.nome.value = TextFieldValue(newValue) },
            label = { Text("Nome do Evento") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        PickDateButton { selectedDate ->
            viewModel.data.value = selectedDate
        }
        Spacer(modifier = Modifier.height(16.dp))

        PickTimeButton({ selectedTime ->
            viewModel.inicio.value = selectedTime
        }, label = "Hora de Início")

        Spacer(modifier = Modifier.height(16.dp))

        PickTimeButton({ selectedTime ->
            viewModel.fim.value = selectedTime
        }, label = "Hora de Fim")

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {

            Button(
                onClick = { onClose() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Cancelar")
            }

            Spacer(Modifier.weight(1f))

            Button(onClick = {
                viewModel.saveEventos()
            }) {
                Text("Adicionar")
            }
        }

        viewModel.toastEvent.observeAsState().value?.let { message ->
            showToast(LocalContext.current, message)
            viewModel.resetToastEvent()
        }
    }
}