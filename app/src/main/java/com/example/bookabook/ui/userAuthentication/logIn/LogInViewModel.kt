package com.example.bookabook.ui.userAuthentication.logIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bookabook.utils.Validation
import com.example.bookabook.utils.ValidationMSG

class LogInViewModel : ViewModel() {

    var userEmail = MutableLiveData<String>()
    private val isEmailValid: LiveData<ValidationMSG> = Transformations.map(userEmail) {
        Validation.validateEmail(it)
    }
    val userEmailError: LiveData<String> = Transformations.map(isEmailValid) {
        Validation.validationResult(it)
    }

    var userPassword = MutableLiveData<String>()
    private val isUserPasswordValid: LiveData<ValidationMSG> = Transformations.map(userPassword) {
        Validation.validatePassword(it)
    }
    val userPasswordError: LiveData<String> = Transformations.map(isUserPasswordValid) {
        Validation.validationResult(it)
    }


    var progressBarVisability = MutableLiveData<Boolean>(false)
    var addBtnEnable = MutableLiveData<Boolean>(true)

    private var _navigateToRegister =MutableLiveData<Boolean>()
    val navigateToRegister : LiveData<Boolean>
    get() = _navigateToRegister

    fun RegisterNow()
    {
        _navigateToRegister.value = true
    }

    fun RegisterNavigationComplete()
    {
        _navigateToRegister.value = false
    }
}

enum class LogInStateState {
    EmailNotValid,
    PasswordNotValid,
    EmailOrPasswordAreNotCorrect,
    LoggedIn
}