package br.com.fiap.startupone.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.fiap.startupone.EventosMarcados
import br.com.fiap.startupone.eventosMock
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen() {
    val currentDate = remember { mutableStateOf(Date()) }
    CalendarView(currentDate, tasks)
}


@Composable
fun CalendarHeader(currentDate: MutableState<Date>) {
    val formattedDate = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("pt", "BR")).format(currentDate.value)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            val cal = Calendar.getInstance()
            cal.time = currentDate.value
            cal.add(Calendar.DAY_OF_YEAR, -1)
            currentDate.value = cal.time
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Day")
        }

        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = {
            val cal = Calendar.getInstance()
            cal.time = currentDate.value
            cal.add(Calendar.DAY_OF_YEAR, 1)
            currentDate.value = cal.time
        }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Day")
        }
    }
}




@Composable
fun CalendarView(currentDate: MutableState<Date>, allTasks: List<EventosMarcados>) {
    val filteredTasks = allTasks.filter { task ->
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.format(task.inicio) == sdf.format(currentDate.value)
    }
    Log.d("DEBUG_TAG", filteredTasks.toString())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            CalendarHeader(currentDate)
            for (index in 0 until 24) {
                CalendarHourRow(hour = index)
            }
        }

        filteredTasks.forEach { task ->
            TaskCardWithOffset(task)
        }
    }
}

@Composable
fun CalendarHourRow(hour: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, Color.Gray)
    ) {
        Text(
            text = "$hour:00",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun TaskCardWithOffset(task: EventosMarcados) {
    val durationMinutes = getEventDurationInMinutes(task)
    val heightDp = (durationMinutes.toFloat()).dp + 4.dp
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .offset(y = getMinutesFromStartOfDay(task.inicio).toFloat().dp + 60.dp)
                .height(heightDp)
                .fillMaxWidth(0.8f)
                .align(Alignment.TopEnd)
                .padding(4.dp)
        ) {
            Text(text = task.nome, modifier = Modifier.padding(8.dp))
        }
    }
}

fun getEventDurationInMinutes(event: EventosMarcados): Long {
    val diffInMillis = event.fim.time - event.inicio.time
    return diffInMillis / (60 * 1000)
}

fun getMinutesFromStartOfDay(date: Date): Long {
    val calendar = Calendar.getInstance().apply {
        time = date
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val diffInMillis = date.time - calendar.timeInMillis
    return diffInMillis / (60 * 1000)
}

val tasks = eventosMock
