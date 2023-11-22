package br.com.fiap.startupone.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.startupone.components.PickTimeButton
import br.com.fiap.startupone.components.SegmentedControl
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.model.InteresseDto
import br.com.fiap.startupone.service.interesses.InteresseServiceFactory
import br.com.fiap.startupone.utils.showToast
import br.com.fiap.startupone.viewmodel.interesses.InteressesVm
import br.com.fiap.startupone.viewmodel.interesses.InteressesVmFactory
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarInteresseForm(
    onClose: () -> Unit,
    interesseToEdit: InteresseDto? = null,
    modalTitle: String,
    periodoInicio: LocalTime = LocalTime.MIDNIGHT,
    periodoFim: LocalTime = LocalTime.MIDNIGHT,
    duracao: LocalTime = LocalTime.MIDNIGHT
){
    val context = LocalContext.current
    val userSessionManager = UserSessionManager.getInstance(context = context)
    val interessesService = InteresseServiceFactory.getInteressesService()
    val viewModel: InteressesVm = viewModel(factory = InteressesVmFactory(userSessionManager, interessesService))

    val prioridades = listOf("Baixa", "Média", "Alta")
    val prioridadeSelecionada by viewModel.prioridadeSelecionada

    LaunchedEffect(interesseToEdit) {
        if (interesseToEdit != null) {
            viewModel.setInteresse(interesseToEdit)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(modalTitle)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.nome.value.text,
            onValueChange = { newValue ->
                if (newValue.length <= 30) {
                    viewModel.nome.value = TextFieldValue(newValue)
                }
            },
            label = { Text("Nome do Interesse") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        PickTimeButton({ selectedTime ->
            viewModel.inicio.value = selectedTime
        }, label = "Periodo de Início", initialTime = if (interesseToEdit != null) interesseToEdit.periodoInicio.toLocalTime() else periodoInicio)

        Spacer(modifier = Modifier.height(16.dp))

        PickTimeButton({ selectedTime ->
            viewModel.fim.value = selectedTime
        }, label = "Periodo de Fim", initialTime = if (interesseToEdit != null) interesseToEdit.periodoFim.toLocalTime() else periodoFim)

        Spacer(modifier = Modifier.height(16.dp))

        PickTimeButton({ selectedTime ->
            viewModel.duracao.value = selectedTime
        }, label = "Duração estimada", initialTime = if (interesseToEdit != null) interesseToEdit.tempoEstimado.toLocalTime() else duracao)

        Spacer(modifier = Modifier.height(16.dp))

        SegmentedControl(
            options = prioridades,
            selectedOption = prioridades[prioridadeSelecionada],
            onOptionSelected = { selectedOption ->
                val index = prioridades.indexOf(selectedOption)
                viewModel.selecionarPrioridade(index)
            },
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {

            Button(
                onClick = { onClose() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Cancelar")
            }

            Spacer(Modifier.weight(1f))

            Button(onClick = {
                if (interesseToEdit != null) viewModel.editarInteresse() else viewModel.salvarInteresse()
            }) {
                Text(if (interesseToEdit != null) "Editar" else "Salvar")
            }
        }

        viewModel.toastEvent.observeAsState().value?.let { message ->
            showToast(LocalContext.current, message)
            viewModel.resetToastEvent()
        }
    }
}