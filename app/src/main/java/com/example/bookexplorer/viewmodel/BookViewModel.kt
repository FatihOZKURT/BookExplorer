package com.example.bookexplorer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexplorer.model.BookDetails
import com.example.bookexplorer.data.repository.BookRepository
import com.example.bookexplorer.model.BookItem
import com.example.bookexplorer.util.toHttps
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _bookDetail = MutableLiveData<BookDetails?>()
    val bookDetail: LiveData<BookDetails?> = _bookDetail

    private val _books = MutableStateFlow<List<BookItem>>(emptyList())
    val books: StateFlow<List<BookItem>> = _books

    private val _categoryBooks = MutableStateFlow<List<BookItem>>(emptyList())
    val categoryBooks: StateFlow<List<BookItem>> = _categoryBooks

    private val _favoriteBooks = MutableStateFlow<List<BookDetails>>(emptyList())
    val favoriteBooks: StateFlow<List<BookDetails>> = _favoriteBooks

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun search(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.searchBooks(query)

                val booksWithImages = response.items?.map { book ->
                    book.apply {
                        volumeInfo.imageLinks?.thumbnail =
                            volumeInfo.imageLinks?.thumbnail?.toHttps().toString()
                    }
                } ?: emptyList()

                _books.value = booksWithImages
            } catch (e: Exception) {
                e.printStackTrace()
                _books.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getFavoriteBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getFavoriteBooks()
                _favoriteBooks.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                _favoriteBooks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getBooksByCategory(category: String) {
        val formattedCategory = "subject:$category"
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getBooksByCategory(formattedCategory)
                _categoryBooks.value = result.items
            } catch (e: Exception) {
                e.printStackTrace()
                _categoryBooks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getBookById(bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getBookById(bookId)
                _bookDetail.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                _bookDetail.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToFavorites(book: BookDetails, onSuccess: () -> Unit) {
        repository.addToFavorites(book, onSuccess)
        _isFavorite.value = true
    }

    fun removeFromFavorites(bookId: String, onSuccess: () -> Unit) {
        repository.removeFromFavorites(bookId, onSuccess)
        getFavoriteBooks()
        _isFavorite.value = false
    }

    fun observeFavoriteStatus(bookId: String) {
        viewModelScope.launch {
            repository.isBookFavorite(bookId).collect { isFav ->
                _isFavorite.value = isFav
            }
        }
    }

}