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
import androidx.compose.runtime.LaunchedEffect
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
import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.service.eventos.EventosServiceFactory
import br.com.fiap.startupone.utils.showToast
import br.com.fiap.startupone.viewmodel.eventos.EventosVm
import br.com.fiap.startupone.viewmodel.eventos.EventosVmFactory
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarEventoForm(
    onClose: () -> Unit,
    eventoToEdit: EventosMarcadosDto? = null,
    modalTitle: String,
    horaInicio: LocalTime = LocalTime.MIDNIGHT,
    horaFim: LocalTime = LocalTime.MIDNIGHT
) {

    val context = LocalContext.current
    val userSessionManager = UserSessionManager.getInstance(context = context)
    val eventoService = EventosServiceFactory.getEventoService()
    val viewModel: EventosVm = viewModel(factory = EventosVmFactory(userSessionManager, eventoService))

    LaunchedEffect(eventoToEdit) {
        if (eventoToEdit != null) {
            viewModel.loadEvent(eventoToEdit)
        } else {
            viewModel.inicio.value = horaInicio
            viewModel.fim.value = horaFim
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(modalTitle)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.nome.value.text,
            onValueChange = { newValue ->
                if (newValue.length <= 30) {
                    viewModel.nome.value = TextFieldValue(newValue)
                }
            },
            label = { Text("Nome do Evento") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        PickDateButton( { selectedDate ->
            viewModel.data.value = selectedDate
        }, initialDate = if (eventoToEdit != null) eventoToEdit.inicio.toLocalDate() else LocalDate.now() )
        Spacer(modifier = Modifier.height(16.dp))

        PickTimeButton({ selectedTime ->
            viewModel.inicio.value = selectedTime
        }, label = "Hora de Início", initialTime = if (eventoToEdit != null) eventoToEdit.inicio.toLocalTime() else horaInicio)

        Spacer(modifier = Modifier.height(16.dp))

        PickTimeButton({ selectedTime ->
            viewModel.fim.value = selectedTime
        }, label = "Hora de Fim", initialTime = if (eventoToEdit != null) eventoToEdit.fim.toLocalTime() else horaFim)


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
                if (eventoToEdit != null) viewModel.editarEvento() else viewModel.salvarEvento()
            }) {
                Text(if (eventoToEdit != null) "Editar" else "Salvar")
            }
        }

        viewModel.toastEvent.observeAsState().value?.let { message ->
            showToast(LocalContext.current, message)
            viewModel.resetToastEvent()
        }
    }
}