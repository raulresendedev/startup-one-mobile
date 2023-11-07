package br.com.fiap.startupone.components.calendario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun CalendarioHeader(
    currentDate: LocalDateTime,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit
) {

    val formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale("pt", "BR"))
    val formattedDate = currentDate.format(formatter)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onPreviousDayClick() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Day")
        }

        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { onNextDayClick() }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Day")
        }
    }
}

