package br.com.fiap.startupone.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.fiap.startupone.R

@Composable
fun CardM3(
        imagePainter: Painter,
        title: String,
        descricao: String,
        modifier: Modifier = Modifier
    ){

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large
    ){
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier.clip(MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .aspectRatio(3f / 2f)
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = descricao,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}