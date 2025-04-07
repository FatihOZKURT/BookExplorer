package com.example.bookexplorer.data

import com.example.bookexplorer.model.BookDetails
import com.example.bookexplorer.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String
    ): BookResponse

    @GET("volumes/{bookId}")
    suspend fun getBookDetails(
        @Path("bookId") bookId: String,
        @Query("key") apiKey: String,
    ): BookDetails

    @GET("volumes")
    suspend fun getBooksByCategory(
        @Query("q") category: String,
        @Query("key") apiKey: String
    ): BookResponse

    @GET("volumes/{volumeId}")
    suspend fun getBookById(
        @Path("volumeId") volumeId: String,
        @Query("key") apiKey: String
    ): BookDetails

}