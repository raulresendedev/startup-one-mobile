package br.com.fiap.startupone.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.com.fiap.startupone.R
import br.com.fiap.startupone.components.CardM3
import coil.compose.rememberImagePainter

@Composable
fun SugestoesScreen() {

    val sugestoes = listOf(
        Sugestao(R.drawable.pedra, "Pedra Grande", "Atibaia - SP"),
        Sugestao(R.drawable.masp, "MASP", "São Paulo - SP"),
        Sugestao(R.drawable.jaragua, "Pico do Jaraguá", "São Paulo - SP")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            sugestoes.forEach { sugestao ->
                CardM3(
                    imagePainter = painterResource(id = sugestao.imageResId),
                    title = sugestao.title,
                    descricao = sugestao.descricao,
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.large)
                        .fillMaxWidth()
                )
            }
        }
    }
}

data class Sugestao(
    val imageResId: Int,
    val title: String,
    val descricao: String
)
