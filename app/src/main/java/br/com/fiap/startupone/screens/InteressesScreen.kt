package br.com.fiap.startupone.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.fiap.startupone.components.CardInteresses
import java.util.Date

@Composable
fun InteressesScreen(){

    val interessesList = listOf(
        Interesse("Ler um livro", Date(123, 8, 17, 8, 5), Date(2023, 9, 17, 9, 10), "Alta"),
        Interesse("Jardinagem", Date(123, 8, 17, 14, 35), Date(2023, 9, 17, 15, 40), "Alta"),
        Interesse("Assistir a um filme", Date(123, 8, 17, 10, 30), Date(2023, 9, 17, 12, 35), "Alta"),
        Interesse("Visitar um museu ou galeria de arte", Date(123, 8, 17, 15, 50), Date(2023, 9, 17, 17, 0), "Alta"),
        Interesse("Cozinhar uma nova receita", Date(123, 8, 17, 14, 10), Date(2023, 9, 17, 15, 45), "Média"),
        Interesse("Caminhar no parque", Date(123, 8, 17, 9, 15), Date(2023, 9, 17, 10, 25), "Média"),
        Interesse("Desenhar ou pintar", Date(123, 8, 17, 13, 10), Date(2023, 9, 17, 14, 30), "Média"),
        Interesse("Ouvir música", Date(123, 8, 17, 13, 0), Date(2023, 9, 17, 14, 5), "Baixa"),
        Interesse("Tocar um instrumento musical", Date(123, 8, 17, 12, 0), Date(2023, 9, 17, 13, 5), "Baixa"),
        Interesse("Fazer um quebra-cabeça", Date(123, 8, 17, 11, 40), Date(2023, 9, 17, 12, 55), "Baixa")
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            interessesList.forEach { interesse ->
                CardInteresses(
                    title = interesse.title,
                    inicio = interesse.inicio,
                    fim = interesse.fim,
                    prioridade = interesse.prioridade,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }
        }
        FloatingActionButton(
            onClick = { /* adicionar interesses */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Localized description")
        }
    }
}

data class Interesse(
    val title: String,
    val inicio: Date,
    val fim: Date,
    val prioridade: String
)