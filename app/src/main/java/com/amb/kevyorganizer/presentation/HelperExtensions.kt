package com.amb.kevyorganizer.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.amb.kevyorganizer.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar

fun showSnackBar(text: String, view: View) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}

fun hideKeyboard(context: Context, view: View) {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions
        .placeholderOf(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}