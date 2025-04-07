package com.example.bookexplorer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.bookexplorer.R
import com.example.bookexplorer.ui.theme.ThemePreference
import com.example.bookexplorer.util.toHttps
import com.example.bookexplorer.viewmodel.BookViewModel


@Composable
fun BookDetailScreen(bookId: String, viewModel: BookViewModel = hiltViewModel(), themePreference: ThemePreference) {
    val bookDetail by viewModel.bookDetail.observeAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val context = LocalContext.current
    val isDarkMode = themePreference.isDarkMode.collectAsState(initial = false)

    LaunchedEffect(bookId) {
        viewModel.getBookById(bookId)
        viewModel.observeFavoriteStatus(bookId)
    }

    Box(modifier = Modifier.fillMaxSize().background(colorScheme.background), contentAlignment = Alignment.Center) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            bookDetail != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            bookDetail?.let {
                                if (isFavorite) {
                                    viewModel.removeFromFavorites(it.id) {
                                        Toast.makeText(context, "Favorilerden çıkarıldı", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    viewModel.addToFavorites(it) {
                                        Toast.makeText(context, "Favorilere eklendi", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.End).padding(top = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.background,
                            contentColor = colorScheme.background
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = if (isFavorite) R.drawable.favourite else R.drawable.addtofavourites),
                            contentDescription = "Favori Butonu",
                            tint = Color.Unspecified
                        )
                    }

                    val imageUrl = bookDetail!!.volumeInfo.imageLinks?.thumbnail?.toHttps()

                    if (imageUrl != null) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Kitap Kapağı",
                            modifier = Modifier
                                .size(500.dp)
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Fit,
                            placeholder = null, // placeholder kullanma
                            error = painterResource(id = R.drawable.book_cover_default) // hata varsa göster
                        )
                    } else {
                        Image(
                            painter = painterResource(id = if (isDarkMode.value) R.drawable.book_cover_dark_mode else R.drawable.book_cover_default),
                            contentDescription = "Kapak Bulunamadı",
                            modifier = Modifier
                                .size(500.dp)
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = bookDetail!!.volumeInfo.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Yazar: ")
                            }
                            append(bookDetail!!.volumeInfo.authors?.joinToString(", ") ?: "Bilinmiyor")
                        },
                        color = colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Kategori: ")
                            }
                            append(bookDetail!!.volumeInfo.categories?.joinToString(", ") ?: "Bilinmiyor")
                        },
                        color = colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Sayfa Sayısı: ")
                            }
                            append(bookDetail!!.volumeInfo.pageCount.toString())
                        },
                        color = colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Yayınevi: ")
                            }
                            append(bookDetail!!.volumeInfo.publisher ?: "Bilinmiyor")
                        },
                        color = colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Basım Tarihi: ")
                            }
                            append(bookDetail!!.volumeInfo.publishedDate ?: "Bilinmiyor")
                        },
                        color = colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = bookDetail!!.volumeInfo.description ?: "Açıklama mevcut değil.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onBackground,
                        textAlign = TextAlign.Justify
                    )

                }
            }
            else -> {
                Text(
                    text = "Kitap bulunamadı.",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
