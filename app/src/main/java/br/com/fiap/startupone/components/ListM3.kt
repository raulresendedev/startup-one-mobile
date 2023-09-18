package br.com.fiap.startupone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.startupone.eventosMock
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEventos() {

    var tasks = eventosMock

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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) {
        val tasksGroupedByDate = tasks.groupBy { it.inicio.toDateString() }

        tasksGroupedByDate.entries.sortedBy { (key, _) -> key }.forEach { (inicio, tasksForDate) ->
            item {
                val displayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(inicio)
                Text(
                    text = displayDate?.toDayMonthString() ?: "",
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 18.sp
                )
            }

            // Display the sorted tasks for that date
            tasksForDate.sortedBy { it.inicio }.let { sortedTasks ->
                items(items = sortedTasks, key = { it.idEventoMarcado }) { task ->
                    ListItem(
                        headlineText = { Text(text = task.nome) },
                        supportingText = { Text(text = "${formatDateToHourMinute(task.inicio)} - ${formatDateToHourMinute(task.fim)}") }
                    )
                    Divider()
                }
            }
        }
    }
}
