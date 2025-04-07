package com.example.bookexplorer.navigation


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.painterResource
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BottomNavigationBar(
    navController: NavController)
{
    var showDialog by remember { mutableStateOf(false) }

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favourites,
        BottomNavItem.Logout
    )

    NavigationBar(containerColor = Color.Unspecified) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = Color.Unspecified
                    )
                },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (item is BottomNavItem.Logout) {
                        showDialog = true
                    } else if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("books_screen") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                    else if(item is BottomNavItem.Home){
                        navController.navigate(item.route)
                    }
                }
            )
        }


    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Çıkış Yap") },
            text = { Text("Gerçekten çıkmak istiyor musun?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login_screen") {
                        popUpTo(0) // tüm stack'i temizle
                    }
                }) {
                    Text("Evet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hayır")
                }
            }
        )
    }
}
