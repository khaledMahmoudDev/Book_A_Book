package com.example.bookabook.ui.userAuthentication.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bookabook.data.FireBaseRepo
import com.example.bookabook.data.RegisterCallBack
import com.example.bookabook.data.RegisterState
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
    private val isUserPasswordValid: LiveData<ValidationMSG> =
        Transformations.map(addUserPassword) {
            Validation.validatePassword(it)
        }
    val userPasswordError: LiveData<String> = Transformations.map(isUserPasswordValid) {
        Validation.validationResult(it)
    }


    var progressBarVisability = MutableLiveData<Boolean>(false)
    var registerBtnEnable = MutableLiveData<Boolean>(true)

    private val _registerState = MutableLiveData<SignUpStateState>()
    val registerState: LiveData<SignUpStateState>
        get() = _registerState



    fun registerNow() {
        when {
            isUserEmailValid.value != ValidationMSG.Good -> {
                _registerState.value = SignUpStateState.EmailNotValid
                return
            }
            isUserNameValid.value != ValidationMSG.Good -> {
                _registerState.value = SignUpStateState.UserNameNotValid
                return
            }
            isUserPhoneValid.value != ValidationMSG.Good -> {
                _registerState.value = SignUpStateState.PhoneNumberNotValid
                return
            }
            isUserPasswordValid.value != ValidationMSG.Good -> {
                _registerState.value = SignUpStateState.PasswordNotValid
                return
            }
            else -> {
                progressBarVisability.value = true
                registerBtnEnable.value = false
                FireBaseRepo.register(
                    email = addUserEmail.value!!,
                    password = addUserPassword.value!!,
                    phoneNumber = addUserPhone.value!!,
                    name = addUserName.value!!,
                    registerCallBack = RegisterCallBack {
                        when (it) {
                            RegisterState.RegisteredSuccessfully ->
                            {

                                _registerState.value = SignUpStateState.Registered
                            }
                            RegisterState.FailedToSignUp ->
                            {

                                _registerState.value = SignUpStateState.FailedToRegister
                            }
                            RegisterState.ErrorEmailOrPassword ->
                            {

                                _registerState.value = SignUpStateState.PasswordOrEmailError
                            }
                        }

                        progressBarVisability.value = false
                        registerBtnEnable.value = true
                    }
                )

            }
        }
    }


}

enum class SignUpStateState {
    UserNameNotValid,
    EmailNotValid,
    PasswordNotValid,
    PhoneNumberNotValid,
    FailedToRegister,
    Registered,
    PasswordOrEmailError
}