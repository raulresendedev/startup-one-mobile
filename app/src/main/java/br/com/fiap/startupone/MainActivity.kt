package br.com.fiap.startupone

import android.content.Intent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.startupone.components.NavigationBarM3
import br.com.fiap.startupone.config.UserSessionManager
import br.com.fiap.startupone.screens.CadastroScreen
import br.com.fiap.startupone.screens.EventosScreen
import br.com.fiap.startupone.screens.HomeScreen
import br.com.fiap.startupone.screens.InteressesScreen
import br.com.fiap.startupone.screens.LoginScreen
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
        val showNavigationBar = remember { mutableStateOf(false) }

        val context = LocalContext.current
        val userSessionManager = UserSessionManager.getInstance(context = context)

        LaunchedEffect(key1 = userSessionManager.isLoggedIn()) {
            if (userSessionManager.isLoggedIn()) {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            } else {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Gray)
        ) {

            NavHost(navController = navController, startDestination = "login") {
                composable(route = "login") {
                    LoginScreen(navController = navController)
                    showNavigationBar.value = false
                }
                composable(route = "cadastro") {
                    CadastroScreen(navController)
                    showNavigationBar.value = false
                }
                composable(route = "home") {
                    HomeScreen()
                    showNavigationBar.value = true
                }
                composable(route = "eventos") {
                    EventosScreen()
                    showNavigationBar.value = true
                }
                composable(route = "interesses") {
                    InteressesScreen()
                    showNavigationBar.value = true
                }
                composable(route = "sugestoes") {
                    SugestoesScreen()
                    showNavigationBar.value = true
                }
            }
        }
        if (showNavigationBar.value) {
            NavigationBarM3(modifier = Modifier.fillMaxWidth(), navController = navController)
        }
    }
}
