package br.com.fiap.startupone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.startupone.components.NavigationBarM3
import br.com.fiap.startupone.screens.EventosScreen
import br.com.fiap.startupone.screens.HomeScreen
import br.com.fiap.startupone.screens.InteressesScreen
import br.com.fiap.startupone.screens.SugestoesScreen
import br.com.fiap.startupone.ui.theme.StartupOneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartupOneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    BoxScreen(navController = navController)
                }
            }
        }
    }
}

@Composable
fun BoxScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Gray)
        ) {
            NavHost(navController = navController, startDestination = "home") {
                composable(route = "home") {
                    HomeScreen()
                }
                composable(route = "eventos") {
                    EventosScreen()
                }
                composable(route = "interesses") {
                    InteressesScreen()
                }
                composable(route = "sugestoes") {
                    SugestoesScreen()
                }
            }
        }
        NavigationBarM3(modifier = Modifier.fillMaxWidth(), navController = navController)
    }
}
