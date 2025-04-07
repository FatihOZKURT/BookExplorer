package com.example.bookexplorer.model

import androidx.compose.ui.graphics.Color

data class Categories(
    val title: String,
    val categoryName: String,
    val color: Color,
    val imageResId: Int
)