package br.com.fiap.startupone.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickDateButton(onDatePicked: (LocalDate) -> Unit, initialDate: LocalDate) {
    var pickedDate by remember { mutableStateOf(initialDate) }
    val formattedDate by derivedStateOf {
        DateTimeFormatter.ofPattern("dd/MM/yyyy").format(pickedDate)
    }

    val dateDialogState = rememberMaterialDialogState()

    OutlinedTextField(
        value = formattedDate,
        onValueChange = {},
        label = { Text("Data") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Ícone de Calendário",
                modifier = Modifier.clickable { dateDialogState.show() }
            )
        },
        readOnly = true,
        enabled = false,
        modifier = Modifier.clickable(onClick = { dateDialogState.show() }),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
    )


    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker(
            initialDate = initialDate,
        ) { selectedDate ->
            pickedDate = selectedDate
            onDatePicked(selectedDate)
        }
    }
}
