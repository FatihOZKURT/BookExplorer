package com.example.bookexplorer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.bookexplorer.R
import com.example.bookexplorer.model.BookDetails
import com.example.bookexplorer.navigation.BottomNavigationBar
import com.example.bookexplorer.ui.theme.ThemePreference
import com.example.bookexplorer.util.toHttps
import com.example.bookexplorer.viewmodel.BookViewModel

@Composable
fun FavouritesScreen(
    viewModel: BookViewModel = hiltViewModel(),
    navController: NavController,
    themePreference: ThemePreference
) {
    val favoriteBooks by viewModel.favoriteBooks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getFavoriteBooks()
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Favoriler",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                favoriteBooks.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Favori kitap listeniz boş.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                else -> {
                    LazyColumn {
                        items(favoriteBooks) { book ->
                            BookItem(
                                book = book,
                                navController = navController,
                                onRemoveClick = { bookId ->
                                    viewModel.removeFromFavorites(bookId) {
                                        Toast.makeText(context, "Favorilerden çıkarıldı", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                themePreference = themePreference
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BookItem(
    book: BookDetails,
    navController: NavController,
    onRemoveClick: (String) -> Unit,
    themePreference: ThemePreference
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                val id = book.id
                navController.navigate("details_screen/$id")

            }
    ) {
        val isDarkMode = themePreference.isDarkMode.collectAsState(initial = false)
        val imageUrl = book.volumeInfo.imageLinks?.thumbnail?.toHttps()

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

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = book.volumeInfo.title, fontWeight = FontWeight.Bold)
            Text(text = book.volumeInfo.authors?.joinToString(", ") ?: "Bilinmiyor")
        }

        IconButton(onClick = { onRemoveClick(book.id) }) {
            Icon(
                painter = painterResource(id = R.drawable.favourite_remove),
                contentDescription = "Favorilerden çıkar",
                tint = Color.Unspecified
            )
        }
    }
}
