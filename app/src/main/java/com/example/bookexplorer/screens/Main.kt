package com.example.bookexplorer.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookexplorer.viewmodel.BookViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.bookexplorer.R
import com.example.bookexplorer.model.BookItem
import com.example.bookexplorer.model.Categories
import com.example.bookexplorer.navigation.BottomNavigationBar
import com.example.bookexplorer.ui.theme.DarkModeSwitch
import com.example.bookexplorer.ui.theme.ThemePreference
import com.example.bookexplorer.util.toHttps
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun BooksScreen(viewModel: BookViewModel = hiltViewModel(),navController: NavController, themePreference: ThemePreference) {

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val kutular = getBoxes()
    val isSearching = searchQuery.text.length > 2
    var showDialog by remember { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val scope = rememberCoroutineScope()
    val isDark = themePreference.isDarkMode.collectAsState(initial = false)

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        if (navBackStackEntry?.destination?.route == "books_screen") {
            searchQuery = TextFieldValue("")
        }
    }

    Scaffold(
        bottomBar = {
                BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        BackHandler(enabled = true) {
            // Geri basınca hiçbir şey yapma
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DarkModeSwitch(
                    checked = isDark.value,
                    modifier = Modifier.padding(8.dp, top = 24.dp),
                    onCheckedChanged = { isChecked ->
                        scope.launch {
                            themePreference.setDarkMode(isChecked)
                        }
                    },
                    switchWidth = 80.dp,
                    switchHeight = 32.dp,
                    handleSize = 24.dp
                )

            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(top = 50.dp, end = 20.dp, start = 20.dp)
        ) {
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it.text.length > 2) {
                            viewModel.search(it.text)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Kitap Ara...", color = colorScheme.onBackground, fontWeight = FontWeight.Bold)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                )
                Spacer(modifier = Modifier.padding(10.dp))
            }

            if (isSearching) {
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    items(books) { book ->
                        BookItemView(book = book, navController = navController)
                    }
                }
            } else {
                items(kutular.chunkedIntoPairs()) { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        for (kutu in rowItems) {
                            BoxItemView(
                                kutu = kutu,
                                navController = navController,
                                modifier = Modifier
                                    .weight(0.5f)
                                    .aspectRatio(1.2f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
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
                        popUpTo("books_screen") { inclusive = true }
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

fun <T> List<T>.chunkedIntoPairs(): List<List<T>> {
    return this.chunked(2)
}

@Composable
fun BookItemView(book: BookItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                val id = book.id
                navController.navigate("details_screen/$id")

            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
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
                    painter = painterResource(id = R.drawable.book_cover_default),
                    contentDescription = "Kapak Bulunamadı",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = book.volumeInfo.title, style = MaterialTheme.typography.titleMedium, color = colorScheme.onBackground)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.volumeInfo.authors?.joinToString(", ") ?: "Bilinmiyor",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


fun getBoxes(): List<Categories> {
    return listOf(
        Categories("Bilim Kurgu","science-fiction", Color.Unspecified, R.drawable.sciencefiction),
        Categories("Dram","drama", Color.Unspecified, R.drawable.drama),
        Categories("Polisiye","Detective", Color.Unspecified, R.drawable.detective),
        Categories("Tarih","history", Color.Unspecified, R.drawable.history),
        Categories("Sanat","art", Color.Unspecified, R.drawable.art),
        Categories("Ekonomi","Economics", Color.Unspecified, R.drawable.economy),
        Categories("Masal","fairytale", Color.Unspecified, R.drawable.fairytale),
        Categories("Felsefe","philosophy", Color.Unspecified, R.drawable.philosophy)
    )
}

@Composable
fun BoxItemView(kutu: Categories, modifier: Modifier = Modifier, navController: NavController) {
    Box(
        modifier = modifier
            .background(kutu.color)
            .padding(top = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = kutu.imageResId),
                contentDescription = kutu.title,
                modifier = Modifier.size(128.dp)
                    .clickable {
                    navController.navigate("category_screen/${kutu.categoryName}")
                }
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(text = kutu.title, fontWeight = FontWeight.Bold, color = colorScheme.onBackground)
        }
    }
}

