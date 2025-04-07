package com.example.bookexplorer.navigation

import androidx.annotation.DrawableRes
import com.example.bookexplorer.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int
) {
    object Home : BottomNavItem("books_screen", "Ana sayfa",  R.drawable.homepage)
    object Favourites : BottomNavItem("favourites_screen", "Favoriler", R.drawable.favouritemenu)
    object Logout : BottomNavItem("logout","Çıkış", R.drawable.logout)
}