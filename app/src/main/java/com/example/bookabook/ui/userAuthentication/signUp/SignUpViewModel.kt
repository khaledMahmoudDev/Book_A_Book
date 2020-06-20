package com.example.bookabook.ui.userAuthentication.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bookabook.utils.Validation
import com.example.bookabook.utils.ValidationMSG

class SignUpViewModel : ViewModel() {

    var addUserName = MutableLiveData<String>()
    private val isUserNameValid: LiveData<ValidationMSG> = Transformations.map(addUserName) {
        Validation.validateInput(it)
    }
    val userNameError: LiveData<String> = Transformations.map(isUserNameValid) {
        Validation.validationResult(it)
    }

    var addUserEmail = MutableLiveData<String>()
    private val isUserEmailValid: LiveData<ValidationMSG> = Transformations.map(addUserEmail) {
        Validation.validateEmail(it)
    }
    val userEmailError: LiveData<String> = Transformations.map(isUserEmailValid) {
        Validation.validationResult(it)
    }

    var addUserPhone = MutableLiveData<String>()
    private val isUserPhoneValid: LiveData<ValidationMSG> = Transformations.map(addUserPhone) {
        Validation.validatePhone(it)
    }
    val userPhoneError: LiveData<String> = Transformations.map(isUserPhoneValid) {
        Validation.validationResult(it)
    }

    var addUserPassword = MutableLiveData<String>()
    private val isUserPasswordValid: LiveData<ValidationMSG> = Transformations.map(addUserPassword) {
        Validation.validatePassword(it)
    }
    val userPasswordError: LiveData<String> = Transformations.map(isUserPasswordValid) {
        Validation.validationResult(it)
    }


}