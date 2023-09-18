package br.com.fiap.startupone.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun CardInteresses(
    title: String,
    inicio: Date,
    fim: Date,
    prioridade: String,
    modifier: Modifier
){
    val hourMinuteFormat = SimpleDateFormat("HH:mm")

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${hourMinuteFormat.format(inicio)} - ${hourMinuteFormat.format(fim)} | Prioridade: $prioridade",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}