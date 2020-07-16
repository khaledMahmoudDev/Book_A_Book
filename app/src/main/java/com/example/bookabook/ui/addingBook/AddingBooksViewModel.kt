package com.example.bookabook.ui.addingBook

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bookabook.R
import com.example.bookabook.data.*
import com.example.bookabook.utils.Validation
import com.example.bookabook.utils.ValidationMSG


class AddingBooksViewModel : ViewModel() {
    private var _categoryList = MutableLiveData<ArrayList<String>>()
    val categoryList: LiveData<ArrayList<String>>
        get() = _categoryList


    private var _addingimageString = MutableLiveData<Uri>()
    val addingimageString: LiveData<Uri>
        get() = _addingimageString

    fun setImageString(imgUri: Uri) {
        _addingimageString.value = imgUri
    }


    private var _startActivityToPickImage = MutableLiveData<Boolean>(false)
    val startActivityToPickImage: LiveData<Boolean>
        get() = _startActivityToPickImage


    private var _imageViewVisibality = MutableLiveData<Boolean>(false)
    val imageViewVisibality: LiveData<Boolean>
        get() = _imageViewVisibality


    var addBookTitle = MutableLiveData<String>()
    private val isBookTitleValid: LiveData<ValidationMSG> = Transformations.map(addBookTitle) {
        Validation.validateInput(it)
    }
    val bookTitleError: LiveData<String> = Transformations.map(isBookTitleValid) {
        Validation.validationResult(it)
    }

    var addBookWriter = MutableLiveData<String>()
    private val isBookWriterValid: LiveData<ValidationMSG> = Transformations.map(addBookWriter) {
        Validation.validateInput(it)
    }
    val bookWriterError: LiveData<String> = Transformations.map(isBookWriterValid) {
        Validation.validationResult(it)
    }

    var addBookDescription = MutableLiveData<String>()
    private val isDescriptionValid: LiveData<ValidationMSG> =
        Transformations.map(addBookDescription) {
            Validation.validateInput(it)
        }
    val descriptionError: LiveData<String> = Transformations.map(isDescriptionValid) {
        Validation.validationResult(it)
    }

    var availabilityState = MutableLiveData<Boolean>(false)
    val availabilityText :LiveData<String> = Transformations.map(availabilityState)
    {
        if (it == true)
        {
            "Available"
        }else
        {
          "Not Available"
        }

    }

    private var _addBookCategories = MutableLiveData<ArrayList<String>>()
    val addBookCategories: LiveData<ArrayList<String>>
        get() = _addBookCategories


    init {
        getCategory()
    }


    private fun getCategory() {
        FireBaseRepo.getCategoriesForChangingData(
            CategoriesData(onDataChanged = {
                _categoryList.value = it
            })
        )
    }

    fun pickImage() {
        _startActivityToPickImage.value = true
    }

    fun pickingImageCompleted() {
        _startActivityToPickImage.value = false
    }

    fun setVisiabilityTrue() {
        _imageViewVisibality.value = true
    }

    fun setVisiabilityFalse() {
        _imageViewVisibality.value = false
    }


    fun showDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_category_dialogue)
        val yesBtn = dialog.findViewById(R.id.Add_yes) as Button
        val noBtn = dialog.findViewById(R.id.add_cancle) as Button
        val editText = dialog.findViewById(R.id.editTextTextPersonName) as EditText
        yesBtn.setOnClickListener {
            if (editText.text.toString().isNotEmpty()) {
                uploadCategory(editText.text.toString())
                dialog.dismiss()
            } else {

                Toast.makeText(context, "please enter category", Toast.LENGTH_LONG).show()
            }
        }
        noBtn.setOnClickListener {

            Toast.makeText(context, "Canceled ", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun uploadCategory(cat: String) {
        FireBaseRepo.upLoadNewCategory(cat)
    }


    private val _addBookState = MutableLiveData<UploadBookState>()
    val addBookState: LiveData<UploadBookState>
        get() = _addBookState

    var progressBarVisability = MutableLiveData<Boolean>(false)
    var addBtnEnable = MutableLiveData<Boolean>(true)

    fun addBtnClick() {
        when {
            isBookTitleValid.value != ValidationMSG.Good -> {
                _addBookState.value = UploadBookState.ErrorBookTitle
                return
            }
            isBookWriterValid.value != ValidationMSG.Good -> {
                _addBookState.value = UploadBookState.ErrorBookWriter
                return
            }
            isDescriptionValid.value != ValidationMSG.Good -> {
                _addBookState.value = UploadBookState.ErrorBookDescription
                return
            }
            _addingimageString.value == null -> {
                _addBookState.value = UploadBookState.ErrorBookImage
                return
            }
            else -> {
                progressBarVisability.value = true
                addBtnEnable.value = false
                FireBaseRepo.uploadBook(
                    UploadBookCallBack { state ->
                        when (state) {
                            BookFireBaseUploadState.BookUploadedSueccessfully -> {

                                _addBookState.value = UploadBookState.Uploaded
                            }
                            BookFireBaseUploadState.BookFailedToUpload -> {

                                _addBookState.value = UploadBookState.FailedToUploadBook

                            }
                            BookFireBaseUploadState.BookImageFailedToUpload -> {
                                _addBookState.value = UploadBookState.FailedToUploadImage
                            }
                        }
                        progressBarVisability.value = false
                        addBtnEnable.value = true
                    },
                    _addingimageString,
                    addBookTitle,
                    addBookWriter,
                    addBookDescription,
                    availabilityState.value!!
                )

            }
        }

//        if (isBookTitleValid.value == ValidationMSG.Good &&
//            isBookWriterValid.value == ValidationMSG.Good &&
//            isDescriptionValid.value == ValidationMSG.Good &&
//            _addingimageString.value != null
//        ) {
//            Log.d("btnClicked", " value is good")
//        } else {
//            Log.d("btnClicked", " value is bad ")
//        }
    }

//    private fun uploadBook(
//        _addingimageString: MutableLiveData<Uri>,
//        addBookTitle: MutableLiveData<String>,
//        addBookWriter: MutableLiveData<String>,
//        addBookDescription: MutableLiveData<String>
//    ) {

//        _addBookState.value = UploadBookState.NoError
//    }


}

enum class UploadBookState {
    ErrorBookTitle,
    ErrorBookWriter,
    ErrorBookDescription,
    ErrorBookImage,
    FailedToUploadBook,
    FailedToUploadImage,
    Uploaded
}