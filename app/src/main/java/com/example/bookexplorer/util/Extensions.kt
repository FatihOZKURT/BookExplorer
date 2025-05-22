package com.example.bookexplorer.util

import android.content.Context
import android.widget.Toast

fun String?.toHttps(): String? {
    return this?.replace("http://", "https://")
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}