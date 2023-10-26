package br.com.fiap.startupone.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}