package br.com.fiap.startupone.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegmentedControl(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier,
) {

    val cornerShape = MaterialTheme.shapes.extraSmall.copy(all = CornerSize(percent = 50))
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    val borderWidth = 1.dp
    val border = BorderStroke(borderWidth, borderColor)

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .then(modifier),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = option == selectedOption

                val buttonShape = when (index) {
                    0 -> cornerShape.copy(bottomEnd = CornerSize(0.dp), topEnd = CornerSize(0.dp))
                    options.size - 1 -> cornerShape.copy(
                        bottomStart = CornerSize(0.dp),
                        topStart = CornerSize(0.dp)
                    )

                    else -> MaterialTheme.shapes.extraSmall.copy(all = CornerSize(0.dp))
                }

                Surface(
                    shape = buttonShape,
                    border = border,
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    tonalElevation = if (isSelected) 3.dp else 0.dp,
                    onClick = { onOptionSelected(option) }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option.replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}