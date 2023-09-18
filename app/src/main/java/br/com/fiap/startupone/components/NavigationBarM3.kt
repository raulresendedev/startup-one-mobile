package br.com.fiap.startupone.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

@Composable
fun NavigationBarM3(modifier: Modifier = Modifier, navController: NavHostController) {

    var selectedItem by remember { mutableStateOf(0) }

    val barItems = listOf(
        BarItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "home"
        ),
        BarItem(
            title = "Eventos",
            selectedIcon = Icons.Filled.DateRange,
            unselectedIcon = Icons.Outlined.DateRange,
            route = "eventos"
        ),
        BarItem(
            title = "Interesses",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            route = "interesses"
        ),
        BarItem(
            title = "Sugestões",
            selectedIcon = Icons.Filled.LocationOn,
            unselectedIcon = Icons.Outlined.LocationOn,
            route = "sugestoes"
        )
    )

    NavigationBar(modifier = modifier) {
        barItems.forEachIndexed { index, barItem ->
            val selected = selectedItem == index
            NavigationBarItem(
                selected = selected,
                onClick = {
                    selectedItem = index
                    if (!selected) navController.navigate(barItem.route)
                },
                icon = {
                    Icon(
                        imageVector = if (selected) barItem.selectedIcon else barItem.unselectedIcon,
                        contentDescription = barItem.title
                    )
                },
                label = { Text(text = barItem.title) },
                alwaysShowLabel = true
            )
        }
    }
}

data class BarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)