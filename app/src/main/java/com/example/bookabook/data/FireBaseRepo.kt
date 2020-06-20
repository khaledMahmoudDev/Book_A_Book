package com.example.bookabook.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookabook.model.BooksModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*
import kotlin.collections.ArrayList

var database: FirebaseDatabase = FirebaseDatabase.getInstance()
var storage: FirebaseStorage = FirebaseStorage.getInstance()
var storageRef: StorageReference = storage.reference


object FireBaseRepo {

    var databaseCategoryRef: DatabaseReference = database.getReference("Categories")
    var databaseBookRef: DatabaseReference = database.getReference("Book")

    fun upLoadNewCategory(cat: String) {
        databaseCategoryRef.push().setValue(cat)
    }

    fun uploadBook( changeState : UploadBookCallBack,
        _addingimageString: MutableLiveData<Uri>,
        addBookTitle: MutableLiveData<String>,
        addBookWriter: MutableLiveData<String>,
        addBookDescription: MutableLiveData<String>
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
                        bookThumbnail = downloadUri.toString()
                    )
                    databaseBookRef.child(id).setValue(book).addOnSuccessListener {
                        Log.d("bookFireBase", "added")
                        changeState.onBookUploadStateChanged(BookFireBaseUploadState.BookUploadedSueccessfully)
                    }.addOnFailureListener {

                        changeState.onBookUploadStateChanged(BookFireBaseUploadState.BookFailedToUpload)
                    }

                }else {
                    changeState.onBookUploadStateChanged(BookFireBaseUploadState.BookImageFailedToUpload)
                }
            }.addOnFailureListener{
                changeState.onBookUploadStateChanged(BookFireBaseUploadState.BookFailedToUpload)

            }


    }

    fun getBooks(downloadBooksCallBack: DownloadBooksCallBack)
    {
        databaseBookRef.addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var bookList : ArrayList<BooksModel> = ArrayList()
                for (n in p0.children)
                {
                    var book = n.getValue(BooksModel::class.java)
                    book?.let { bookList.add(it) }
                }
                downloadBooksCallBack.onDataSuccess(bookList)
            }

        })
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
    val onDataChanged: (dataChanged: ArrayList<BooksModel>) -> Unit
) {
    fun onDataSuccess(data: ArrayList<BooksModel>) = onDataChanged(data)
}

open class UploadBookCallBack(
    val bookUploadState: (stateFireBase: BookFireBaseUploadState) -> Unit
) {
    fun onBookUploadStateChanged(changeStateFireBase : BookFireBaseUploadState) = bookUploadState(changeStateFireBase)
}

enum class BookFireBaseUploadState{
    BookUploadedSueccessfully,
    BookFailedToUpload,
    BookImageFailedToUpload
}