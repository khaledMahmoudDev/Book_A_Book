package com.example.bookabook.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookabook.model.BooksModel
import com.example.bookabook.model.BooksModelRetreving
import com.example.bookabook.model.User
import com.example.bookabook.model.fcm.DataMessage
import com.example.bookabook.model.fcm.FireBaseCloudMessage
import com.example.bookabook.network.FCMClient
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


var database: FirebaseDatabase = FirebaseDatabase.getInstance()
var storage: FirebaseStorage = FirebaseStorage.getInstance()
var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
var storageRef: StorageReference = storage.reference


object FireBaseRepo {
    private const val TAG = "FireBaseRepo"

    var databaseCategoryRef: DatabaseReference = database.getReference("Categories")
    var databaseBookRef: DatabaseReference = database.getReference("Book")
    var databaseUserRef: DatabaseReference = database.getReference("Users")
    var databaseBooksFavouriteRef: DatabaseReference = database.getReference("FavouriteBooks")


    fun areTheSameUSer(email: String) = email == mAuth.currentUser!!.email


    fun register(
        email: String,
        password: String,
        name: String,
        registerCallBack: RegisterCallBack
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                task.addOnSuccessListener { it ->
                    val id = it.user!!.uid
                    createUserOnDB(email, name, id, RegisterCallBack {
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
        id: String,
        registerCallBack: RegisterCallBack
    ) {
        //  val id = databaseUserRef.push().key.toString()
        var user = User(userName = name, email = email, id = id)
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

    fun removeBook(bookId: String) {
        Log.d("fav", "fire remove")
        databaseBookRef.child(bookId).removeValue()
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
        checkedItemList: ArrayList<String>,
        _pickFileString: MutableLiveData<Uri>
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
                        bookOwnerId = mAuth.currentUser!!.email!!,
                        bookCategory = checkedItemList,
                        bookFile = _pickFileString.value.toString()
                    )

                    databaseBookRef.child(id).setValue(book).addOnSuccessListener {
                        Log.d("bookFireBase", "added")
                        changeState.onBookUploadStateChanged(BookFireBaseUploadState.BookUploadedSueccessfully)
                        sendNotifcaion(book)
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

    private fun sendNotifcaion(book: BooksModel) {
        val headers: HashMap<String, String> = HashMap()
        headers.put("Content-Type", "application/json")
        headers.put(
            "Authorization",
            "key=AAAA1fON-EU:APA91bFdJ525H0gM7fu8vhoZbO2B5unN1nZU5b5ulqu3eE1ild1KCgS2IvD6QvBjD2nUHgYgnVhdzmYYu6z4JDlv_BMAC0Jwq3bLFdyLHazH5KEo_rDX_qmiVZ8-WvyMP4w9BSN1RElT"
        )

        val data = DataMessage(
            id = book.id,
            bookTitle = book.bookTitle,
            bookWriter = book.bookWriter,
            bookThumbnail = book.bookThumbnail
        )
        val messageBody = FireBaseCloudMessage("/topics/NEW_BOOK", data)


        val call = FCMClient.fcmMessage.sendNewPostNotification(headers, messageBody)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val url = call.request().url().toString()
                val body = call.request().body().toString()
                val headrs = call.request().headers().toString()
                Log.d(TAG, "onResponse: sent   \n url $url \n $body \n $headrs")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onResponse: failed")
            }

        })
    }


    fun getUser(usercallBack: UserCallBack) {

        databaseUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(p0: DataSnapshot) {
                for (n in p0.children) {
                    val us = n.getValue(User::class.java)
                    if (mAuth.currentUser!!.uid == us!!.id) {

                        usercallBack.onGettingUser(us)
                        Log.d("fav", "user is $us")
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
                            book.let { bookList.add(0, it) }
                        }
                        OwnerCondition.CurrentUser -> {
                            book.let {
                                if (it.bookOwnerId == mAuth.currentUser!!.email) {
                                    bookList.add(0, it)
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
    fun onBookRetrievedSuccessFully(dataChange: BooksModelRetreving) = dataChanged(dataChange)

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

