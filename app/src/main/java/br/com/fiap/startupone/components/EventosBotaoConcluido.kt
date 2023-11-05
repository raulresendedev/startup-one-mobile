package br.com.fiap.startupone.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.fiap.startupone.model.EventosMarcadosDto

@Composable
fun EventosBotaoConcluido(evento: EventosMarcadosDto, onToggleCompletion: (EventosMarcadosDto) -> Unit) {
    val icon = Icons.Default.CheckCircle
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(.7f)
    val borderWidth = 2.dp
    val backgroundColor = Color.Transparent

    IconButton(onClick = {
        onToggleCompletion(evento)
    }) {
        Surface(
            color = backgroundColor,
            shape = CircleShape,
            border = BorderStroke(borderWidth, borderColor)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = if (evento.concluido) "Desmarcar" else "Marcar como concluído",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
