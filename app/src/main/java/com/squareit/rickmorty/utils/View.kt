package com.squareit.rickmorty.utils

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snack(root: View, message: String) {
    Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
}

fun Any.lok(message: Any) {
    Log.d("TAG", "RESULT $message")
}