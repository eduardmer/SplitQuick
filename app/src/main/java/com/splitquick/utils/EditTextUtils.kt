package com.splitquick.utils

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.content(): String {
    return text.toString().trim()
}

fun TextInputEditText.isEmpty(): Boolean {
    return text.toString().trim().isEmpty()
}