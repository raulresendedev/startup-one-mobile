package br.com.fiap.startupone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventosHeader(dataString: String){
    Text(
        text = dataString,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        fontSize = 18.sp
    )
}