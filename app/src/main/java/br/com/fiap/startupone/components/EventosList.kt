package br.com.fiap.startupone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.forms.AdicionarEventoForm
import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.service.eventos.EventosServiceFactory
import br.com.fiap.startupone.utils.showToast
import br.com.fiap.startupone.viewmodel.eventos.EventosVm
import br.com.fiap.startupone.viewmodel.eventos.EventosVmFactory
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.setValue


@OptIn(ExperimentalMaterial3Api::class)
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

    fun formatDateToHourMinute(date: Date): String {
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    fun Date.toDateString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(this)
    }

    fun Date.toDayMonthString(): String {
        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        return dateFormat.format(this)
    }

    fun closeDialog() {
        viewModel.resetFormFields()
        showDialog.value = false
        selectedEventForEdit.value = null
    }

    if (isLoading) {
        LoadingSpinner("Procurando seus eventos...")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val tasksGroupedByDate = tasks.groupBy {
                Date.from(it.inicio.atZone(ZoneId.systemDefault()).toInstant()).toDateString()
            }

            tasksGroupedByDate.entries.sortedBy { (key, _) -> key }
                .forEach { (inicio, tasksForDate) ->
                    item {
                        val displayDate = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).parse(inicio)
                        Text(
                            text = displayDate?.toDayMonthString() ?: "",
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 18.sp
                        )
                    }

                    tasksForDate.sortedBy { it.inicio }.let { sortedTasks ->
                        items(items = sortedTasks, key = { it.idEventoMarcado }) { task ->
                            val inicioDate: Date =
                                Date.from(task.inicio.atZone(ZoneId.systemDefault()).toInstant())
                            val fimDate: Date =
                                Date.from(task.fim.atZone(ZoneId.systemDefault()).toInstant())
                            ListItem(
                                modifier = Modifier.fillMaxWidth(),
                                headlineText = { Text(text = task.nome) },
                                supportingText = {
                                    Text(
                                        text = "${formatDateToHourMinute(inicioDate)} - ${
                                            formatDateToHourMinute(
                                                fimDate
                                            )
                                        }"
                                    )
                                },
                                trailingContent = {
                                    var showMenu by remember { mutableStateOf(false) }
                                    IconButton(onClick = { showMenu = true }) {
                                        Icon(
                                            imageVector = Icons.Filled.MoreVert,
                                            contentDescription = "Ações"
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false }
                                    ) {
                                        DropdownMenuItem(onClick = {
                                            showDialog.value = true
                                            selectedEventForEdit.value = task
                                            showMenu = false
                                        }, text = { Text("Editar") })
                                        DropdownMenuItem(onClick = {
                                            viewModel.deleteEvento(task)
                                        }, text = { Text("Excluir") })
                                    }
                                } )
                            Divider()
                        }
                    }
                }
            }
        }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = { showDialog.value = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Localized description")
        }

        viewModel.toastEvent.observeAsState().value?.let { message ->
            showToast(LocalContext.current, message)
        }
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
1

    val eventoAdicionado by viewModel.eventoAdicionado.observeAsState(initial = false)

    if (eventoAdicionado) {
        closeDialog()
        viewModel.eventoAdicionado.value = false
    }

}
