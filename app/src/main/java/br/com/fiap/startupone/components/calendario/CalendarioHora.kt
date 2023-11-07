package br.com.fiap.startupone.components.calendario

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CalendarioHora(
    hour: Int,
    onClick: (Int) -> Unit,
    altura: Dp = 90.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(altura)
            .border(1.dp, Color.Gray)
            .clickable(onClick = { onClick(hour) })
    ) {
        Text(
            text = "$hour:00",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp)
        )
    }
}