package com.example.bookabook.ui.favouriteBooks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bookabook.data.*
import com.example.bookabook.model.BooksModelRetreving

class FavouriteBooksViewModel : ViewModel() {
    private var _bookList = MutableLiveData<ArrayList<BooksModelRetreving>>()
    val booklist : LiveData<ArrayList<BooksModelRetreving>>
        get() = _bookList


    val isEmpty = Transformations.map(_bookList){
        it.isNullOrEmpty()
    }

    init {
    }

     fun getBooksList(){
        FireBaseRepo.getAllFavBooks(FavouriteBooksCallBack {
            bookList->
            val bookListRet = ArrayList<BooksModelRetreving>()
            bookList.forEach { bookId ->
                Log.d("myFav", "book Id $bookId")
                FireBaseRepo.getBook(bookId, FavBookCallback {
                    bookListRet.add(it)
                    _bookList.value = bookListRet

                })
            }
        })
    }
}