package br.com.fiap.startupone.components.interesses

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.fiap.startupone.model.InteresseDto
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun CardInteresses(
    interesse: InteresseDto,
    prioridadeSelecionada: String,
    modifier: Modifier,
    onEditEvent: (InteresseDto) -> Unit,
    onDeleteEvent: (InteresseDto) -> Unit
){
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large
    ){
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = interesse.nome,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Horário: ${interesse.periodoInicio.toLocalTime()} - ${interesse.periodoFim.toLocalTime()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Prioridade: $prioridadeSelecionada",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row {

                var showMenu by remember { mutableStateOf(false) }
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Ações"
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(onClick = {
                        showMenu = false
                        onEditEvent(interesse)
                    }, text = { Text("Editar") })
                    DropdownMenuItem(onClick = {
                        showMenu = false
                        onDeleteEvent(interesse)
                    }, text = { Text("Excluir") })
                }
            }
        }
    }
}
