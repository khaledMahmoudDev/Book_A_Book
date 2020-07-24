package com.example.bookabook.ui.elementDetails

import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookabook.data.FavouriteBooksCallBack
import com.example.bookabook.data.FireBaseRepo
import com.example.bookabook.data.storage
import com.example.bookabook.model.BooksModelRetreving
import com.example.bookabook.model.User
import java.io.File

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


    var askForWritePermission = MutableLiveData<Boolean>(false)
    fun completeAskForPermission() {
        askForWritePermission.value = false
    }


    fun checkPermissionForDownLoad() {
        askForWritePermission.value = true
    }


    lateinit var bookFile: File

    var openFileNow = MutableLiveData<Boolean>(false)

    fun completeOpenFile() {
        openFileNow.value = false
    }

    fun downLoadFile() {

        Log.d("firebase file", "clicked")
        if (receivedBook.value!!.bookFile.isNotEmpty()) {
            val ref = storage.getReferenceFromUrl(receivedBook.value!!.bookFile)

            val rootPath =
                File(Environment.getExternalStorageDirectory(), "Book A Book DOWNLOADS")
            if (!rootPath.exists()) {
                rootPath.mkdirs();
            }
            val localFile = File(rootPath, "${receivedBook.value!!.bookTitle}.pdf")
            ref.getFile(localFile)
                .addOnSuccessListener {
                    bookFile = localFile
                    openFileNow.value = true
                    Log.d("firebase file", "local tem file created  created $localFile")
                }.addOnFailureListener {

                    Log.d("firebase file", "Failure")
                }.addOnProgressListener {

                    val progress = ((100.0 * it.bytesTransferred) / it.totalByteCount).toInt()

                    Log.d("firebase file", "Upload is $progress% done")

                }
        } else {
            Log.d("firebase file", "No File Found")
        }
    }

    val navigateToSimilarBooks = MutableLiveData<Boolean>(false)
    fun navigateToSimilarBooksClick()
    {
        navigateToSimilarBooks.value = true
    }
    fun navigateToSimilarBooksComplete ()
    {
        navigateToSimilarBooks.value = false
    }


    fun removeSelectedBook()
    {
        FireBaseRepo.removeBook(receivedBook.value!!.id)

    }



}