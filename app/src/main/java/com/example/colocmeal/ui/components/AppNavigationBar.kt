package com.example.colocmeal.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class NavDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val iconSelected: ImageVector = icon
)

@Composable
fun AppNavigationBar(
    destinations: List<NavDestination>,
    currentRoute: String?,
    onSelect: (NavDestination) -> Unit
) {
    NavigationBar {
        destinations.forEach { dest ->
            val selected = dest.route == currentRoute
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(dest) },
                icon = {
                    Icon(
                        if (selected) dest.iconSelected else dest.icon,
                        contentDescription = dest.label
                    )
                },
                label = { Text(dest.label) }
            )
        }
    }
}