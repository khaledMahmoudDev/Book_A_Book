package com.example.bookabook.ui.similarBooks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bookabook.data.DownloadBooksCallBack
import com.example.bookabook.data.FireBaseRepo
import com.example.bookabook.data.OwnerCondition
import com.example.bookabook.model.BooksModelRetreving

class SimilarBooksViewModel(book: BooksModelRetreving) : ViewModel() {

    var receivedBook = MutableLiveData<BooksModelRetreving>()


    private var _basicBookList = MutableLiveData<ArrayList<BooksModelRetreving>>()

    var bookList = Transformations.map(_basicBookList) {
        filterByCategory()
    }


    init {
        receivedBook.value = book
        getBooksList()
    }

    private fun getBooksList() {
        FireBaseRepo.getBooks(OwnerCondition.AllUsers, DownloadBooksCallBack {
            _basicBookList.value = it

        })
    }

    private fun filterByCategory(): List<BooksModelRetreving>? {
        val list = ArrayList<BooksModelRetreving>()
        if (receivedBook.value?.bookCategory?.isNotEmpty()!!) {
            receivedBook.value!!.bookCategory.forEach { cat ->
                _basicBookList.value?.forEach {
                    if (it.bookCategory.contains(cat)) {
                        if (!list.contains(it) && it != receivedBook.value) {
                            list.add(it)
                        }
                    }
                }
            }
        }
        return list as List<BooksModelRetreving>
    }
}