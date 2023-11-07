package br.com.fiap.startupone.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.startupone.components.LoadingSpinner
import br.com.fiap.startupone.components.calendario.CalendarioHeader
import br.com.fiap.startupone.components.calendario.CalendarioList
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.forms.AdicionarEventoForm
import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.service.eventos.EventosServiceFactory
import br.com.fiap.startupone.utils.showToast
import br.com.fiap.startupone.viewmodel.eventos.EventosVm
import br.com.fiap.startupone.viewmodel.eventos.EventosVmFactory
import java.time.LocalTime

@Composable
fun HomeScreen() {

    val context = LocalContext.current
    val userSessionManager = UserSessionManager.getInstance(context)
    val eventosService = EventosServiceFactory.getEventoService()
    val viewModel: EventosVm = viewModel(factory = EventosVmFactory(userSessionManager, eventosService))
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val eventos by viewModel.eventosLiveData.observeAsState(initial = emptyList())
    val currentDate by viewModel.currentDate.collectAsState()

    val showDialog = remember { mutableStateOf(false) }
    val selectedHourForAdd = remember { mutableStateOf<Int?>(null) }
    val selectedEventForEdit = remember { mutableStateOf<EventosMarcadosDto?>(null) }

    val updateDate: (Long) -> Unit = { value ->
        viewModel.currentDate.value = viewModel.currentDate.value.plusDays(value)
        viewModel.carregarEventosData()
    }

    val onHourClick: (Int) -> Unit = { hour ->
        selectedHourForAdd.value = hour
        showDialog.value = true
    }

    val onEventoClick: (EventosMarcadosDto) -> Unit = { evento ->
        selectedEventForEdit.value = evento
        showDialog.value = true
    }


    LaunchedEffect(key1 = true) {
        viewModel.carregarEventosData()
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column {
            CalendarioHeader(
                currentDate = currentDate,
                onPreviousDayClick = { updateDate(-1) },
                onNextDayClick = { updateDate(1) }
            )
            if(isLoading){
                LoadingSpinner(loadingText = "Buscando eventos...")
            }else{
                CalendarioList(
                    eventos = eventos,
                    onHourClick = onHourClick,
                    onEventoClick = onEventoClick,
                    onToggleCompletion = { eventoAtualizado, onResult ->
                        viewModel.atualizarConclusaoEvento(eventoAtualizado) { success ->
                            onResult(success)
                        }
                    }
                )
            }
        }
    }

    fun closeDialog() {
        showDialog.value = false
        selectedHourForAdd.value = null
        selectedEventForEdit.value = null
        viewModel.resetFormFields()
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = { closeDialog() }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(1.dp)
            ) {
                when {
                    selectedEventForEdit.value != null -> {
                        AdicionarEventoForm(
                            onClose = { closeDialog() },
                            eventoToEdit = selectedEventForEdit.value,
                            modalTitle = "Editar Evento"
                        )
                    }
                    selectedHourForAdd.value != null -> {
                        AdicionarEventoForm(
                            onClose = { closeDialog() },
                            modalTitle = "Adicionar Evento",
                            horaInicio = LocalTime.of(selectedHourForAdd.value!!, 0),
                            horaFim = if(selectedHourForAdd.value != 23) LocalTime.of(selectedHourForAdd.value!!+1, 0) else LocalTime.of(selectedHourForAdd.value!!, 59)
                        )
                    }
                }
            }
        }
    }

    val eventoAdicionado by viewModel.eventoAdicionado.observeAsState(initial = false)

    if (eventoAdicionado) {
        closeDialog()
        viewModel.eventoAdicionado.value = false
        viewModel.carregarEventosData()
    }

    viewModel.toastEvent.observeAsState().value?.let { message ->
        showToast(LocalContext.current, message)
    }
}
