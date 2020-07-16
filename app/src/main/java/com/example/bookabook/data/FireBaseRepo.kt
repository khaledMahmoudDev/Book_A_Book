package com.example.bookabook.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookabook.model.BooksModel
import com.example.bookabook.model.BooksModelRetreving
import com.example.bookabook.model.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance
import java.util.*
import kotlin.collections.ArrayList

var database: FirebaseDatabase = FirebaseDatabase.getInstance()
var storage: FirebaseStorage = FirebaseStorage.getInstance()
var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
var storageRef: StorageReference = storage.reference


object FireBaseRepo {

    var databaseCategoryRef: DatabaseReference = database.getReference("Categories")
    var databaseBookRef: DatabaseReference = database.getReference("Book")
    var databaseUserRef: DatabaseReference = database.getReference("Users")
    var databaseBooksFavouriteRef: DatabaseReference = database.getReference("FavouriteBooks")


    fun areTheSameUSer(email: String) = email == mAuth.currentUser!!.email


    fun register(
        email: String,
        password: String,
        name: String,
        phoneNumber: String,
        registerCallBack: RegisterCallBack
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                task.addOnSuccessListener { it ->
                    val id = it.user!!.uid
                    createUserOnDB(email, name, phoneNumber, id, RegisterCallBack {
                        when (it) {
                            RegisterState.UserSavedSuccessfully -> {
                                registerCallBack.onLogInStateChange(RegisterState.RegisteredSuccessfully)
                            }
                            else -> {
                                registerCallBack.onLogInStateChange(RegisterState.FailedToSignUp)
                            }
                        }

                    })
                }.addOnFailureListener {
                    registerCallBack.onLogInStateChange(RegisterState.ErrorEmailOrPassword)
                    Log.d("error", "sd$it")
                }
            }.addOnFailureListener {
                registerCallBack.onLogInStateChange(RegisterState.FailedToSignUp)
            }

    }

    private fun createUserOnDB(
        email: String,
        name: String,
        phoneNumber: String,
        id: String,
        registerCallBack: RegisterCallBack
    ) {
        //  val id = databaseUserRef.push().key.toString()
        var user = User(userName = name, email = email, phoneNumber = phoneNumber, id = id)
        databaseUserRef.child(id).setValue(user).addOnSuccessListener {
            sendVerificationEmail(
                RegisterCallBack {
                    when (it) {
                        RegisterState.SendVerificationSuccessfully -> {
                            registerCallBack.onLogInStateChange(RegisterState.UserSavedSuccessfully)
                        }
                        else -> {
                            registerCallBack.onLogInStateChange(RegisterState.FailedToSaveUser)
                        }

                    }

                })
        }
    }


    private fun sendVerificationEmail(registerCallBack: RegisterCallBack) {
        val user = mAuth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener { task ->
            task.addOnSuccessListener { registerCallBack.onLogInStateChange(RegisterState.SendVerificationSuccessfully) }
                .addOnFailureListener { registerCallBack.onLogInStateChange(RegisterState.FailedToSendVerification) }
        }
    }

    fun logIn(email: String, password: String, logInCallBack: LogInCallBack) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                task.addOnSuccessListener {
                    logInCallBack.onLogInStateChange(verifyEmail())

                }.addOnFailureListener {
                    logInCallBack.onLogInStateChange(LogInState.EmailOrPasswordError)
                    Log.d("logInerror", "sd$it")

                }
            }.addOnFailureListener {
                logInCallBack.onLogInStateChange(LogInState.FailedToLogIn)
            }

    }

    private fun verifyEmail(): LogInState {
        val user = mAuth.currentUser
        return if (user!!.isEmailVerified) {
            LogInState.LoggedInSuccessfully
        } else {
            LogInState.EmailIsNotVerified
        }
    }

    fun isLoggedIn() = mAuth.currentUser != null

    fun forgotPassword(email: String) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            task.addOnSuccessListener { }.addOnFailureListener { }
        }
    }

    fun signOut() {
        mAuth.signOut()
        Log.d("stateLogOut", "logInFirebase")
    }

    fun uploadNewFavBook(bookId: String) {

        databaseBooksFavouriteRef.child(mAuth.currentUser!!.uid).child(bookId).setValue(bookId)
    }

    fun removeFavBooks(bookId: String) {
        Log.d("fav", "fire remove")
        databaseBooksFavouriteRef.child(mAuth.currentUser!!.uid).child(bookId).removeValue()
    }



    fun getAllFavBooks(favouriteBooksCallBack: FavouriteBooksCallBack) {
        databaseBooksFavouriteRef.child(mAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {

                    var favBooksList: ArrayList<String> = ArrayList()
                    for (n in p0.children) {
                        var favBook: String? = n.getValue(String::class.java)
                        if (favBook != null) {
                            favBooksList.add(favBook)
                        }
                    }
                    favouriteBooksCallBack.onGettingFavouriteBook(favBooksList)
                }

            })
    }

//
//    fun getAddedBook(bookCallBack : FavBookCallback)
//    {
//        databaseBookRef.addChildEventListener(object: ChildEventListener{
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
//
//            }
//
//            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//
//            }
//
//            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//                val book = p0.getValue(BooksModelRetreving::class.java)
//                Log.d("onChildAdded", "$book")
//                if (book != null) {
//                    bookCallBack.onBookRetrievedSuccessFully(book)
//                }
//            }
//
//            override fun onChildRemoved(p0: DataSnapshot) {
//
//            }
//
//        })
//
//    }
    fun getBook(bookId: String, bookDataCallBack: FavBookCallback) {
        Log.d("fav", "cancelled")
        databaseBookRef.child(bookId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("fav retrived", "cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val booksModelRetreving = p0.getValue(BooksModelRetreving::class.java)
                if (booksModelRetreving != null) {
                    Log.d("fav retrived", booksModelRetreving.toString())
//                    val x = getTimeDate(booksModelRetreving.bookAddedDate)
//                    booksModelRetreving.bookAddedDateString = x!!
                    bookDataCallBack.onBookRetrievedSuccessFully(booksModelRetreving)
                } else {
                    Log.d("fav retrived", "false ot null")
                }

            }

        })
    }


    fun upLoadNewCategory(cat: String) {
        databaseCategoryRef.push().setValue(cat)
    }

    fun uploadBook(
        changeState: UploadBookCallBack,
        _addingimageString: MutableLiveData<Uri>,
        addBookTitle: MutableLiveData<String>,
        addBookWriter: MutableLiveData<String>,
        addBookDescription: MutableLiveData<String>,
        availability: Boolean
    ) {

        val ref = storageRef.child("uploads/" + UUID.randomUUID().toString())
        val uploadTask = ref.putFile(_addingimageString.value!!)
        val urlTask =
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val id = databaseBookRef.push().key.toString()
                    val book: BooksModel = BooksModel(
                        id = id,
                        bookTitle = addBookTitle.value!!,
                        bookWriter = addBookWriter.value!!,
                        bookDescription = addBookDescription.value!!,
                        bookThumbnail = downloadUri.toString(),
                        bookAddedDate = ServerValue.TIMESTAMP,
                        bookAvailability = availability,
                        bookOwnerId = mAuth.currentUser!!.email!!
                    )
                    databaseBookRef.child(id).setValue(book).addOnSuccessListener {
                        Log.d("bookFireBase", "added")
                        changeState.onBookUploadStateChanged(BookFireBaseUploadState.BookUploadedSueccessfully)
                    }.addOnFailureListener {

                        changeState.onBookUploadStateChanged(BookFireBaseUploadState.BookFailedToUpload)
                    }

                } else {
                    changeState.onBookUploadStateChanged(BookFireBaseUploadState.BookImageFailedToUpload)
                }
            }.addOnFailureListener {
                changeState.onBookUploadStateChanged(BookFireBaseUploadState.BookFailedToUpload)

            }


    }


    fun getUser(usercallBack: UserCallBack) {
        lateinit var user: User
        databaseUserRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(p0: DataSnapshot) {
                for (n in p0.children) {
                    val us = n.getValue(User::class.java)
                    if (mAuth.currentUser!!.uid == us!!.id) {
                        user = User()
                        usercallBack.onGettingUser(user)
                        Log.d("fav", "user is $user")
                    }
                }
            }

        })

    }

    fun getBooks(ownerCondition: OwnerCondition, downloadBooksCallBack: DownloadBooksCallBack) {
        databaseBookRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var bookList: ArrayList<BooksModelRetreving> = ArrayList()
                for (n in p0.children) {
                    var book = n.getValue(BooksModelRetreving::class.java)
                    val x = getTimeDate(book!!.bookAddedDate)
                    book.bookAddedDateString = x!!
                    val serverTime = Date(book.bookAddedDate).time
                    val days = getNumOfDays(serverTime)
                    if (days >= 1) {
                        book.isNew = false
                    }

                    Log.d("timeadded ", " time $days avail ${book.isNew}")
                    when (ownerCondition) {
                        OwnerCondition.AllUsers -> {
                            book.let { bookList.add(it) }
                        }
                        OwnerCondition.CurrentUser -> {
                            book.let {
                                if (it.bookOwnerId == mAuth.currentUser!!.email) {
                                    bookList.add(it)
                                }
                            }
                        }
                    }


                }
                downloadBooksCallBack.onDataSuccess(bookList)
            }

        })
    }

    private fun getNumOfDays(serverTime: Long): Long {
        val currenet = Date().time
        val diffTimeStamp = currenet - serverTime
        return diffTimeStamp / (1000 * 60 * 60 * 20)
    }

    fun getTimeDate(timestamp: Long): String? {
        return try {
            val dateFormat: DateFormat = getDateTimeInstance()
            val netDate = Date(timestamp)
            dateFormat.format(netDate)
        } catch (e: Exception) {
            "date"
        }
    }

    fun getCategoriesForChangingData(categoriesData: CategoriesData) {

        databaseCategoryRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var catList: ArrayList<String> = ArrayList()
                for (n in p0.children) {
                    var cat: String? = n.getValue(String::class.java)
                    if (cat != null) {
                        catList.add(cat)
                    }
                }
                categoriesData.onDataSuccess(catList)
            }
        })
    }
}

open class CategoriesData(
    val onDataChanged: (dataChanged: ArrayList<String>) -> Unit
) {
    fun onDataSuccess(data: ArrayList<String>) = onDataChanged(data)
}


open class DownloadBooksCallBack(
    val onDataChanged: (dataChanged: ArrayList<BooksModelRetreving>) -> Unit
) {
    fun onDataSuccess(data: ArrayList<BooksModelRetreving>) = onDataChanged(data)
}

open class UploadBookCallBack(val bookUploadState: (stateFireBase: BookFireBaseUploadState) -> Unit) {
    fun onBookUploadStateChanged(changeStateFireBase: BookFireBaseUploadState) =
        bookUploadState(changeStateFireBase)
}

open class LogInCallBack(val logInState: (state: LogInState) -> Unit) {
    fun onLogInStateChange(changeState: LogInState) = logInState(changeState)
}


class FavBookCallback(val dataChanged: (data: BooksModelRetreving) -> Unit) {
    fun onBookRetrievedSuccessFully( dataChange :BooksModelRetreving) = dataChanged(dataChange)

}

enum class BookFireBaseUploadState {
    BookUploadedSueccessfully,
    BookFailedToUpload,
    BookImageFailedToUpload
}

enum class LogInState {
    EmailIsNotVerified,
    FailedToLogIn,
    EmailOrPasswordError,
    LoggedInSuccessfully
}

open class RegisterCallBack(val registerState: (state: RegisterState) -> Unit) {
    fun onLogInStateChange(changeState: RegisterState) = registerState(changeState)
}

class UserCallBack(val userstate: (user: User) -> Unit) {
    fun onGettingUser(userChange: User) = userstate(userChange)

}

class FavouriteBooksCallBack(val FavouriteBookState: (book: ArrayList<String>) -> Unit) {
    fun onGettingFavouriteBook(FavouriteBookChange: ArrayList<String>) =
        FavouriteBookState(FavouriteBookChange)

}

enum class RegisterState {
    FailedToSignUp,
    ErrorEmailOrPassword,
    RegisteredSuccessfully,
    FailedToSendVerification,
    SendVerificationSuccessfully,
    FailedToSaveUser,
    UserSavedSuccessfully
}

enum class OwnerCondition {
    AllUsers,
    CurrentUser
}

