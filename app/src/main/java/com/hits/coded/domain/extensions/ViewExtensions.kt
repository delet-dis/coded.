package com.hits.coded.domain.extensions

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE)
                as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val inputMethodManager =
        context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE)
                as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}