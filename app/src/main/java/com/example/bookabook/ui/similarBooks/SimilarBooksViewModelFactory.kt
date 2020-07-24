package com.example.bookabook.ui.similarBooks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookabook.model.BooksModelRetreving

class SimilarBooksViewModelFactory (private val book : BooksModelRetreving) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SimilarBooksViewModel::class.java))
        {
            return SimilarBooksViewModel(book) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}