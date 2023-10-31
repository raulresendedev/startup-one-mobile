package br.com.fiap.startupone.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import br.com.fiap.startupone.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickTimeButton(onTimePicked: (LocalTime) -> Unit, label: String) {
    var pickedTime by remember { mutableStateOf(LocalTime.MIDNIGHT.withSecond(0).withNano(0)) }
    val formattedTime by derivedStateOf {
        DateTimeFormatter.ofPattern("HH:mm").format(pickedTime)
    }

    val timeDialogState = rememberMaterialDialogState()

    OutlinedTextField(
        value = formattedTime,
        onValueChange = {},
        label = { Text(label) },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_access_time_24),
                contentDescription = "Ícone de Relógio"
            )
        },
        readOnly = true,
        enabled = false,
        modifier = Modifier.clickable(onClick = { timeDialogState.show() }),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
    )

    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        timepicker(
            initialTime = LocalTime.MIDNIGHT,
            is24HourClock = true

        ) { selectedTime ->
            pickedTime = selectedTime
            onTimePicked(selectedTime)
        }
    }
}
