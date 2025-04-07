package com.example.bookexplorer.util

fun String?.toHttps(): String? {
    return this?.replace("http://", "https://")
}