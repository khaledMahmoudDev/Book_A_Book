package com.example.bookabook.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bookabook.data.DownloadBooksCallBack
import com.example.bookabook.data.FireBaseRepo
import com.example.bookabook.data.OwnerCondition
import com.example.bookabook.model.BooksModelRetreving

class SearchViewModel : ViewModel() {
//    private var _bookList = MutableLiveData<ArrayList<BooksModelRetreving>>()

    private var bookList = ArrayList<BooksModelRetreving>()

    private val query = MutableLiveData<String>()

    fun setQuery(text: String) {
        query.value = text
    }

    var result: LiveData<List<BooksModelRetreving>> = Transformations.map(query)
    {
        filter(it)
    }

    private fun filter(query: String?): List<BooksModelRetreving> {

        return if (query!!.isNotEmpty()) {
            bookList.filter { it.bookTitle.toLowerCase().contains(query.toLowerCase()) ||
                        it.bookWriter.toLowerCase().contains(query.toLowerCase())
            }


        } else {
            bookList
        }

    }

    init {
        getBooksList()
    }


    private fun getBooksList() {
        FireBaseRepo.getBooks(OwnerCondition.AllUsers, DownloadBooksCallBack {
            bookList = it
        })
    }


}