package com.example.bookabook.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookabook.data.DownloadBooksCallBack
import com.example.bookabook.data.FavBookCallback
import com.example.bookabook.data.FireBaseRepo
import com.example.bookabook.data.OwnerCondition
import com.example.bookabook.model.BooksModelRetreving

class HomeViewModel : ViewModel() {

    private var _bookList = MutableLiveData<ArrayList<BooksModelRetreving>>()
        val booklist : LiveData<ArrayList<BooksModelRetreving>>
        get() = _bookList


    var progressBarVisability = MutableLiveData<Boolean>(false)

    init {
        getBooksList()
    }



    private fun getBooksList(){
        progressBarVisability.value = true
        FireBaseRepo.getBooks(OwnerCondition.AllUsers,DownloadBooksCallBack {
            _bookList.value = it
            progressBarVisability.value = false
        })
    }


    private var _navigateToLogIn =MutableLiveData<Boolean>()
    val navigateToLogIn : LiveData<Boolean>
        get() = _navigateToLogIn

    fun navigateToLogIn()
    {
        _navigateToLogIn.value = true
    }

    fun navigateToLogInComplete()
    {
        _navigateToLogIn.value = false
    }

}