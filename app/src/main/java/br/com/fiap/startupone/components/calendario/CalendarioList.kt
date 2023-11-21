package br.com.fiap.startupone.components.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.fiap.startupone.model.EventosMarcadosDto

@Composable
fun CalendarioList(
    eventos: List<EventosMarcadosDto>,
    onHourClick: (Int) -> Unit,
    onEventoClick: (EventosMarcadosDto) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            for (index in 0 until 24) {
                CalendarioHora(index, onHourClick)
            }
        }

        eventos.forEach { task ->
            CalendarioEvento(task, onEventoClick)
        }
    }
}