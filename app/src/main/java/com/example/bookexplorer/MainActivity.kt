package com.example.bookexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.bookexplorer.navigation.AppNavigation
import com.example.bookexplorer.ui.theme.BookExplorerTheme
import com.example.bookexplorer.ui.theme.ThemePreference
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val themePreference = ThemePreference(applicationContext)

        lifecycleScope.launch {
            themePreference.isDarkMode.collect { isDark ->
                setContent {
                    val navController = rememberNavController()
                    val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null
                    val startDestination = if (isUserLoggedIn) "books_screen" else "login_screen"

                    BookExplorerTheme(darkTheme = isDark) {
                        AppNavigation(
                            navController = navController,
                            themePreference = themePreference,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }

    }
}

