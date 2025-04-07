package com.example.bookexplorer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.bookexplorer.R
import com.example.bookexplorer.model.BookItem
import com.example.bookexplorer.ui.theme.ThemePreference
import com.example.bookexplorer.util.toHttps
import com.example.bookexplorer.viewmodel.BookViewModel

@Composable
fun CategoryBookScreen(categoryName: String ,viewModel: BookViewModel = hiltViewModel(), navController: NavController,themePreference: ThemePreference){
    val categoryBooks by viewModel.categoryBooks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    LaunchedEffect(categoryName) {
        viewModel.getBooksByCategory(categoryName)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 50.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when {
            isLoading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            categoryBooks.isEmpty() -> {
                item {

                    Text(
                        text = "Bu kategoride kitap bulunamadı.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                items(categoryBooks) { book ->
                    CategoryBookItemView(categoryBooks = book, navController = navController,themePreference = themePreference)
                }
            }
        }
        }
    }
}



@Composable
fun CategoryBookItemView(categoryBooks: BookItem, navController: NavController,themePreference: ThemePreference) {
    val isDarkMode = themePreference.isDarkMode.collectAsState(initial = false)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                val id = categoryBooks.id
                navController.navigate("details_screen/$id")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val imageUrl = categoryBooks.volumeInfo.imageLinks?.thumbnail?.toHttps()

            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Kitap Kapağı",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.book_cover_default)
                )
            } else {
                Image(
                    painter = painterResource(id = if (isDarkMode.value) R.drawable.book_cover_dark_mode else R.drawable.book_cover_default),
                    contentDescription = "Kapak Bulunamadı",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp)
                )
            }
            Column {
                Text(text = categoryBooks.volumeInfo.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = categoryBooks.volumeInfo.authors?.joinToString(", ") ?: "Bilinmiyor",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}