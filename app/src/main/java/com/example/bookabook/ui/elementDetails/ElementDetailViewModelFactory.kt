package com.example.bookabook.ui.elementDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookabook.model.BooksModelRetreving

class ElementDetailViewModelFactory(private val book : BooksModelRetreving) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElementDetailsViewModel::class.java))
        {
            return ElementDetailsViewModel(book) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}