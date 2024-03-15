package com.splitquick.utils

import androidx.appcompat.widget.SearchView

fun SearchView.filterData(block: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            block(newText ?: "")
            return false
        }
    })
}