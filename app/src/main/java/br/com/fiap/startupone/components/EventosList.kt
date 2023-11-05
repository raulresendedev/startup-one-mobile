package br.com.fiap.startupone.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.viewmodel.eventos.EventosVm
import br.com.fiap.startupone.config.DateUtils.toDayMonthString
import br.com.fiap.startupone.config.DateUtils.toLocalDateString
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ListEventos(
    viewModel: EventosVm,
    onEditEvent: (EventosMarcadosDto) -> Unit
) {

    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val eventos by viewModel.eventosLiveData.observeAsState(emptyList())

    if (isLoading) {
        LoadingSpinner("Procurando seus eventos...")
    } else if (eventos.isEmpty()) {
        EventosEmpty()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {

            val eventosAgrupadosPorData = eventos.groupBy { it.inicio.toLocalDateString() }

            eventosAgrupadosPorData.entries.sortedBy { (key, _) -> key }
                .forEach { (inicio, eventosNaData) ->
                    item {
                        val inicioDate =
                            LocalDate.parse(inicio, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        EventosHeader(dataString = inicioDate.toDayMonthString())
                    }

                    eventosNaData.sortedBy { it.inicio }.let { eventosOrdenados ->
                        items(items = eventosOrdenados, key = { it.idEventoMarcado }) { evento ->
                            EventosListItem(
                                evento = evento,
                                onEditEvent = onEditEvent,
                                onDeleteEvent = { eventoSelecionado ->
                                    viewModel.deletarEvento(eventoSelecionado)
                                },
                                onToggleCompletion = { eventoSelecionado ->
                                    viewModel.atualizarConclusaoEvento(eventoSelecionado)
                                }
                            )
                        }
                    }
                }
        }
    }
}
