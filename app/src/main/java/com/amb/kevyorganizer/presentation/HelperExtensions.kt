package com.amb.kevyorganizer.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileInputStream

fun getBitmap(path: String): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        bitmap = BitmapFactory.decodeStream(FileInputStream(File(path)), null, options)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}

fun showSnackBar(text: String, view: View) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}

fun hideKeyboard(context: Context, view: View) {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}