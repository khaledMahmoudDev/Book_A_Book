package com.example.bookabook.ui.elementDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookabook.data.FavouriteBooksCallBack
import com.example.bookabook.data.FireBaseRepo
import com.example.bookabook.model.BooksModelRetreving
import com.example.bookabook.model.User

class ElementDetailsViewModel(book: BooksModelRetreving) : ViewModel() {
    var receivedBook = MutableLiveData<BooksModelRetreving>()

    val isLoggedIn = MutableLiveData<Boolean>(false)
    val sameUser = MutableLiveData<Boolean>(false)

    val currentUser = MutableLiveData<User>()
    val favBooks = MutableLiveData<ArrayList<String>>()

    val isBookFav = MutableLiveData<Boolean>(false)

//    val toggleButtonState : LiveData<Boolean>
//    get() = isBookFav

    init {
        receivedBook.value = book
        checkLogging()
    }

    fun checkLogging() {
        if (FireBaseRepo.isLoggedIn()) {
            isLoggedIn.value = true
            checkAreTheSameUser()
            getFavouriteBooks()
        } else {
            isLoggedIn.value = false
        }

    }


    private fun getFavouriteBooks() {
        FireBaseRepo.getAllFavBooks(FavouriteBooksCallBack {
            isBookFav.value = it.contains(receivedBook.value!!.id)
            Log.d("fav", "get Fav${isBookFav.value}")

        })

    }

    val navigateToLogIn = MutableLiveData<Boolean>(false)
    fun addToFav() {
        if (isLoggedIn.value!!) {
            if (isBookFav.value!!) {
                Log.d("fav", "on click true${isBookFav.value}")
                FireBaseRepo.uploadNewFavBook(receivedBook.value!!.id)
                isBookFav.value = false
            } else {
                Log.d("fav", "on click false ${isBookFav.value}")
                FireBaseRepo.removeFavBooks(receivedBook.value!!.id)

                isBookFav.value = true
            }
        } else {
            navigateToLogIn.value = true
        }

    }

    fun completeNavigateToLogIn() {
        navigateToLogIn.value = false
    }

    private fun checkAreTheSameUser() {
        sameUser.value = FireBaseRepo.areTheSameUSer(receivedBook.value!!.bookOwnerId)
    }


}