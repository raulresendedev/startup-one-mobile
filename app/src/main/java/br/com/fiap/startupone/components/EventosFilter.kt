package br.com.fiap.startupone.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import br.com.fiap.startupone.viewmodel.eventos.EventosVm


@Composable
fun EventosFilter(viewModel: EventosVm) {
    val filtros = listOf("futuros", "atrasados", "concluidos")

    val selectedIndex by viewModel.selectedIndex

    SegmentedControl(
        options = filtros,
        selectedOption = filtros[selectedIndex],
        onOptionSelected = { selectedOption ->
            val index = filtros.indexOf(selectedOption)
            viewModel.setSelectedFilter(index)
        },
        modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)
    )
}


