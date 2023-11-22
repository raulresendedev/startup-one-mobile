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
import br.com.fiap.startupone.components.ActionButton
import br.com.fiap.startupone.components.eventos.EventosFilter
import br.com.fiap.startupone.components.eventos.ListEventos
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.forms.AdicionarEventoForm
import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.service.eventos.EventosServiceFactory
import br.com.fiap.startupone.utils.showToast
import br.com.fiap.startupone.viewmodel.eventos.EventosVm
import br.com.fiap.startupone.viewmodel.eventos.EventosVmFactory

@Composable
fun EventosScreen() {

    val context = LocalContext.current
    val userSessionManager = UserSessionManager.getInstance(context)
    val eventosService = EventosServiceFactory.getEventoService()

    val viewModel: EventosVm = viewModel(factory = EventosVmFactory(userSessionManager, eventosService))

    val selectedEventForEdit = remember { mutableStateOf<EventosMarcadosDto?>(null) }

    val showDialog = remember { mutableStateOf(false) }

    fun closeDialog() {
        showDialog.value = false
        selectedEventForEdit.value = null
        viewModel.resetFormFields()
    }

    LaunchedEffect(key1 = true) {
        viewModel.carregarEventos()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            EventosFilter(viewModel = viewModel)
            ListEventos(
                viewModel = viewModel,
                onEditEvent = { evento ->
                    selectedEventForEdit.value = evento
                    showDialog.value = true
                }
            )
        }

        ActionButton(
            onClick = { showDialog.value = true }
        )
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = {
            closeDialog()
        }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(1.dp)
            ) {
                AdicionarEventoForm(
                    onClose = { closeDialog() },
                    eventoToEdit = selectedEventForEdit.value,
                    modalTitle = if (selectedEventForEdit.value != null) "Editar Evento" else "Adicionar Evento"
                )
            }
        }
    }

    val eventoAdicionado by viewModel.eventoAdicionado.observeAsState(initial = false)

    if (eventoAdicionado) {
        closeDialog()
        viewModel.eventoAdicionado.value = false
        viewModel.carregarEventos()
    }

    viewModel.toastEvent.observeAsState().value?.let { message ->
        showToast(LocalContext.current, message)
    }
}
