package com.example.bookabook.ui.myBooks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookabook.data.DownloadBooksCallBack
import com.example.bookabook.data.FireBaseRepo
import com.example.bookabook.data.OwnerCondition
import com.example.bookabook.model.BooksModelRetreving

class MyBooksViewModel : ViewModel() {
    private var _bookList = MutableLiveData<ArrayList<BooksModelRetreving>>()
    val booklist : LiveData<ArrayList<BooksModelRetreving>>
        get() = _bookList

    init {

    }

     fun getBooksList(){
        FireBaseRepo.getBooks(OwnerCondition.CurrentUser,DownloadBooksCallBack {
            _bookList.value = it
        })
    }
}