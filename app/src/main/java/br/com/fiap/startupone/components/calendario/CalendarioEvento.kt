package br.com.fiap.startupone.components.calendario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.fiap.startupone.components.eventos.EventosBotaoConcluido
import br.com.fiap.startupone.model.EventosMarcadosDto
import java.time.Duration
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

fun getEventDurationInMinutes(event: EventosMarcadosDto): Long {
    val inicioInstant = event.inicio.atZone(ZoneId.systemDefault()).toInstant()
    val fimInstant = event.fim.atZone(ZoneId.systemDefault()).toInstant()

    val duration = Duration.between(inicioInstant, fimInstant)
    return duration.toMinutes() * 2
}

fun getMinutesFromStartOfDay(date: Date, lineHeightDp: Float): Float {
    val calendar = Calendar.getInstance().apply {
        time = date
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val diffInMillis = date.time - calendar.timeInMillis
    val minutesFromStartOfDay = diffInMillis / (60 * 1000)
    return minutesFromStartOfDay * (lineHeightDp / 60)
}

@Composable
fun CalendarioEvento(
    evento: EventosMarcadosDto,
    onClick: (EventosMarcadosDto) -> Unit,
    altura: Dp = 90.dp,
    onToggleCompletion: (EventosMarcadosDto, (Boolean) -> Unit) -> Unit
) {

    val durationMinutes = getEventDurationInMinutes(evento)

    val heightDp = (durationMinutes.toFloat()).dp

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val inicio: Date = Date.from(evento.inicio.atZone(ZoneId.systemDefault()).toInstant())

        Card(
            modifier = Modifier
                .offset(y = getMinutesFromStartOfDay(inicio, altura.value).dp)
                .heightIn(min = heightDp)
                .fillMaxWidth(0.8f)
                .align(Alignment.TopEnd)
                .clickable(onClick = { onClick(evento) }),
            elevation = CardDefaults.elevatedCardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = evento.nome,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}