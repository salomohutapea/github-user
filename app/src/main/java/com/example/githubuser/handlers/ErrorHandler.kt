package com.example.githubuser.handlers

import android.content.Context
import android.widget.Toast

class ErrorHandler {
    fun errorMessage(code: Int, context: Context) {
        if (code in 400..443 && code in 445..598) {
            Toast.makeText(context, "$this Cannot fetch data", Toast.LENGTH_SHORT).show()
        } else if (code == 444) {
            Toast.makeText(context, "No connection", Toast.LENGTH_SHORT).show()
        }
    }
}