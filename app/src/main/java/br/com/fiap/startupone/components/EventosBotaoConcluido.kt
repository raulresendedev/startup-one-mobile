package br.com.fiap.startupone.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.fiap.startupone.model.EventosMarcadosDto

@Composable
fun EventosBotaoConcluido(evento: EventosMarcadosDto, onToggleCompletion: (EventosMarcadosDto) -> Unit) {
    val icon = Icons.Default.CheckCircle
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val borderWidth = 2.dp
    val backgroundColor = Color.Transparent

    var concluido by remember { mutableStateOf(evento.concluido) }

    val iconOpacity by animateFloatAsState(
        targetValue = if (concluido) 1f else 0f,
        animationSpec = tween(
            durationMillis = 150,
            easing = FastOutSlowInEasing
        )
    )

    IconButton(onClick = {

        concluido = !concluido

        onToggleCompletion(evento.copy(concluido = concluido))
    }) {
        Surface(
            color = backgroundColor,
            shape = CircleShape,
            border = BorderStroke(borderWidth, borderColor)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = if (concluido) "Desmarcar" else "Marcar como concluído",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = iconOpacity)
            )
        }
    }
}

