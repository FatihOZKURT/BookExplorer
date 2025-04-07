package com.example.bookexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bookexplorer.screens.BookDetailScreen
import com.example.bookexplorer.screens.BooksScreen
import com.example.bookexplorer.screens.CategoryBookScreen
import com.example.bookexplorer.screens.FavouritesScreen
import com.example.bookexplorer.screens.LoginScreen
import com.example.bookexplorer.screens.RegisterScreen
import com.example.bookexplorer.ui.theme.ThemePreference

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    themePreference: ThemePreference) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login_screen") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("books_screen") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                }, navController = navController
            )
        }

        composable("register_screen") {
            RegisterScreen( onLoginSuccess = {
                navController.navigate("books_screen") {
                    popUpTo("register_screen") { inclusive = true }
                }
            }, navController = navController
            )
        }

        composable("books_screen") {
            BooksScreen(navController = navController, themePreference = themePreference)
        }

        composable("favourites_screen") {
            FavouritesScreen(navController = navController)
        }

        composable("details_screen/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookDetailScreen(bookId = bookId,themePreference= themePreference)
        }

        composable("category_screen/{categoryName}") { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryBookScreen(categoryName = categoryName, navController = navController, themePreference = themePreference)
        }
    }
}