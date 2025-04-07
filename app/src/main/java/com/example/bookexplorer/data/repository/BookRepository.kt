package com.example.bookexplorer.data.repository

import com.example.bookexplorer.data.GoogleBooksApiService
import com.example.bookexplorer.model.BookDetails
import com.example.bookexplorer.model.BookResponse
import com.example.bookexplorer.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val apiService: GoogleBooksApiService
) {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore

    suspend fun searchBooks(query: String): BookResponse {
        return apiService.searchBooks(query, Constants.API_KEY)
    }

    suspend fun getBooksByCategory(category: String): BookResponse {
        return apiService.getBooksByCategory(category, Constants.API_KEY)
    }

   suspend fun getBookById(bookId: String): BookDetails {
        return apiService.getBookById(bookId, Constants.API_KEY)
    }

    fun isBookFavorite(bookId: String): StateFlow<Boolean> {
        val uid = auth.currentUser?.uid ?: return MutableStateFlow(false)
        val result = MutableStateFlow(false)

        firestore.collection("users")
            .document(uid)
            .collection("favourites")
            .document(bookId)
            .addSnapshotListener { snapshot, _ ->
                result.value = snapshot?.exists() == true
            }

        return result
    }

    fun addToFavorites(book: BookDetails, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val favData = mapOf("bookId" to book.id)

        firestore.collection("users")
            .document(uid)
            .collection("favourites")
            .document(book.id)
            .set(favData)
            .addOnSuccessListener { onSuccess() }
    }

    fun removeFromFavorites(bookId: String, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(uid)
            .collection("favourites")
            .document(bookId)
            .delete()
            .addOnSuccessListener { onSuccess() }
    }

    suspend fun getFavoriteBookIds(): List<String> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("favourites")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.getString("bookId") }
    }
    suspend fun getFavoriteBooks(): List<BookDetails> {
        val bookIds = getFavoriteBookIds()
        return bookIds.mapNotNull { id ->
            try {
                getBookById(id)
            } catch (e: Exception) {
                null
            }
        }
    }

}