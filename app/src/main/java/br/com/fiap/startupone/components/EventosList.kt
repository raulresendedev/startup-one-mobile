package br.com.fiap.startupone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.forms.AdicionarEventoForm
import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.service.eventos.EventosServiceFactory
import br.com.fiap.startupone.utils.showToast
import br.com.fiap.startupone.viewmodel.eventos.EventosVm
import br.com.fiap.startupone.viewmodel.eventos.EventosVmFactory
import br.com.fiap.startupone.config.DateUtils.toDayMonthString
import br.com.fiap.startupone.config.DateUtils.toLocalDateString
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ListEventos() {
    val context = LocalContext.current
    val userSessionManager = UserSessionManager.getInstance(context)
    val eventosService = EventosServiceFactory.getEventoService()

    val viewModel: EventosVm =
        viewModel(factory = EventosVmFactory(userSessionManager, eventosService))

    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val tasks by viewModel.eventosLiveData.observeAsState(emptyList())
    val showDialog = remember { mutableStateOf(false) }
    val selectedEventForEdit = remember { mutableStateOf<EventosMarcadosDto?>(null) }

    fun closeDialog() {
        viewModel.resetFormFields()
        showDialog.value = false
        selectedEventForEdit.value = null
    }

    if (isLoading) {
        LoadingSpinner("Procurando seus eventos...")
    } else if (tasks.isEmpty()) {
        EventosEmpty()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val eventosAgrupadosPorData = tasks.groupBy { it.inicio.toLocalDateString() }

            eventosAgrupadosPorData.entries.sortedBy { (key, _) -> key }
                .forEach { (inicio, eventosNaData) ->
                    item {
                        val inicioDate =
                            LocalDate.parse(inicio, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        EventosHeader(dataString = inicioDate.toDayMonthString())
                    }

                    eventosNaData.sortedBy { it.inicio }.let { eventosOrdenados ->
                        items(items = eventosOrdenados, key = { it.idEventoMarcado }) { evento ->
                            EventosListItem(
                                evento = evento,
                                onEditEvent = { eventoSelecionado ->
                                    showDialog.value = true
                                    selectedEventForEdit.value = eventoSelecionado
                                },
                                onDeleteEvent = { eventoSelecionado ->
                                    viewModel.deleteEvento(eventoSelecionado)
                                }
                            )
                        }
                    }
                }
        }
    }

    ActionButton {
        showDialog.value = true
    }

    viewModel.toastEvent.observeAsState().value?.let { message ->
        showToast(LocalContext.current, message)

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
    }

}
