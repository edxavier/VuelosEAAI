package com.edxavier.vueloseaai.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavHostController
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
    ){
        items.forEach {item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    // Avoid nav history on navbar
                    navController.navigate(item.route){
                        val route = navController.currentBackStackEntry?.destination?.route
                        route?.apply {
                            popUpTo(route) {
                                inclusive =  true
                            }
                        }
                        launchSingleTop = true
                    }
                },
                label = {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.Light,
                        fontSize = 11.sp
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "${item.name} Icon",
                        modifier = Modifier.size(20.dp)
                    )
                },

            )
        }
    }
}