package br.com.fiap.startupone.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.fiap.startupone.model.EventosMarcadosDto
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventosListItem(
    evento: EventosMarcadosDto,
    onEditEvent: (EventosMarcadosDto) -> Unit,
    onDeleteEvent: (EventosMarcadosDto) -> Unit,
    onToggleCompletion: (EventosMarcadosDto, (Boolean) -> Unit) -> Unit
){
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        headlineText = { Text(text = evento.nome) },
        supportingText = {
            Text(
                text = "${evento.inicio.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${
                    evento.fim.format(DateTimeFormatter.ofPattern("HH:mm"))
                }"
            )
        },
        trailingContent = {
            Row {

                EventosBotaoConcluido(evento=evento, onToggleCompletion=onToggleCompletion)

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
                        onEditEvent(evento)
                    }, text = { Text("Editar") })
                    DropdownMenuItem(onClick = {
                        showMenu = false
                        onDeleteEvent(evento)
                    }, text = { Text("Excluir") })
                }
            }
        }
    )
    Divider()
}