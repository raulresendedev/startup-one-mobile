package br.com.fiap.startupone.components.interesses

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.startupone.model.EventosMarcadosDto
import br.com.fiap.startupone.model.InteresseDto
import br.com.fiap.startupone.viewmodel.interesses.InteressesVm

@Composable
fun InteressesList(
    viewModel: InteressesVm,
    onEditInteresse: (InteresseDto) -> Unit
) {
    val interesses by viewModel.interessesLiveData.observeAsState(emptyList())

    if (interesses.isEmpty()) {
        InteressesEmpty()
    } else {
        LazyColumn {
            items(interesses.size, key = { index -> interesses[index].idInteresse }) { index ->
                val interesse = interesses[index]
                CardInteresses(
                    interesse = interesse,
                    prioridadeSelecionada = viewModel.prioridades(interesse.prioridade),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onEditEvent = onEditInteresse,
                    onDeleteEvent = { viewModel.deletarInteresse(interesse) }
                )
            }
        }
    }
}
