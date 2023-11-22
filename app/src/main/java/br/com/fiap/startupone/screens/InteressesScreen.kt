package br.com.fiap.startupone.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.startupone.components.ActionButton
import br.com.fiap.startupone.components.interesses.InteressesList
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.forms.AdicionarInteresseForm
import br.com.fiap.startupone.model.InteresseDto
import br.com.fiap.startupone.service.interesses.InteresseServiceFactory
import br.com.fiap.startupone.utils.showToast
import br.com.fiap.startupone.viewmodel.interesses.InteressesVm
import br.com.fiap.startupone.viewmodel.interesses.InteressesVmFactory

@Composable
fun InteressesScreen(){

    val context = LocalContext.current
    val userSessionManager = UserSessionManager.getInstance(context)
    val interesseService = InteresseServiceFactory.getInteressesService()

    val viewModel: InteressesVm = viewModel(factory = InteressesVmFactory(userSessionManager, interesseService))

    val selectedInteresseForEdit = remember { mutableStateOf<InteresseDto?>(null) }

    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.carregarInteresses()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {

        InteressesList(
            viewModel,
            onEditInteresse = { interesse ->
                selectedInteresseForEdit.value = interesse
                showDialog.value = true
            }
        )

        ActionButton(
            onClick = { showDialog.value = true }
        )
    }

    fun closeDialog() {
        showDialog.value = false
        selectedInteresseForEdit.value = null
        viewModel.resetFormFields()
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = {
            closeDialog()
        }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(1.dp)
            ) {
                AdicionarInteresseForm(
                    onClose = { closeDialog() },
                    interesseToEdit = selectedInteresseForEdit.value,
                    modalTitle = if (selectedInteresseForEdit.value != null) "Editar Interesse" else "Adicionar Interesse"
                )
            }
        }
    }

    val interesseAdicionado by viewModel.interesseAdicionado.observeAsState(initial = false)

    if (interesseAdicionado) {
        closeDialog()
        viewModel.interesseAdicionado.value = false
        viewModel.carregarInteresses()
    }

    viewModel.toastEvent.observeAsState().value?.let { message ->
        showToast(LocalContext.current, message)
    }
}
