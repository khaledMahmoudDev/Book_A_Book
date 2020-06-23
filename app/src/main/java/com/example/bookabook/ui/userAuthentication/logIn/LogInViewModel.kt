package com.example.bookabook.ui.userAuthentication.logIn

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.bookabook.R
import com.example.bookabook.data.FireBaseRepo
import com.example.bookabook.data.LogInCallBack
import com.example.bookabook.data.LogInState
import com.example.bookabook.utils.Validation
import com.example.bookabook.utils.ValidationMSG

class LogInViewModel : ViewModel() {

    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED  ,        // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    private fun isLoggedIn() = FireBaseRepo.isLoggedIn()

    val authenticationState = MutableLiveData<AuthenticationState>()

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    init {

        if (isLoggedIn())
        {
            authenticationState.value = AuthenticationState.AUTHENTICATED
        }else
        {
            authenticationState.value = AuthenticationState.UNAUTHENTICATED
        }
    }


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
    var logInEnable = MutableLiveData<Boolean>(true)

    private val _logInState = MutableLiveData<LogInStateState>()
    val logInState: LiveData<LogInStateState>
        get() = _logInState

    fun logIn() {
        when {
            isEmailValid.value != ValidationMSG.Good -> {
                _logInState.value = LogInStateState.EmailNotValid
            }
            isUserPasswordValid.value != ValidationMSG.Good -> {

                _logInState.value = LogInStateState.PasswordNotValid
            }
            else -> {
                progressBarVisability.value = true
                logInEnable.value = false
                FireBaseRepo.logIn(email = userEmail.value!!, password = userPassword.value!!,
                    logInCallBack = LogInCallBack {
                        when (it) {
                            LogInState.EmailIsNotVerified -> {
                                _logInState.value = LogInStateState.EmailIsNotVerified
                                authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                            }
                            LogInState.FailedToLogIn -> {
                                _logInState.value = LogInStateState.FAiledToLogIn
                                authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                            }
                            LogInState.EmailOrPasswordError -> {
                                _logInState.value = LogInStateState.EmailOrPasswordIsNotCorrect
                                authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                            }
                            LogInState.LoggedInSuccessfully -> {
                                _logInState.value = LogInStateState.LoggedIn
                                authenticationState.value = AuthenticationState.AUTHENTICATED
                            }
                        }
                        progressBarVisability.value = false
                        logInEnable.value = true
                    })
            }
        }
    }


    fun forgetPasswordShowDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.forgot_password_dialogue)
        val yesBtn = dialog.findViewById(R.id.Add_yes) as Button
        val noBtn = dialog.findViewById(R.id.add_cancle) as Button
        val editText = dialog.findViewById(R.id.editTextTextPersonName) as EditText
        yesBtn.setOnClickListener {
            var toastMsg = ""
            when (Validation.validateEmail(editText.text.toString())) {
                ValidationMSG.Empty -> {
                    toastMsg = "email can not be empty"
                }
                ValidationMSG.NotEmail -> {
                    toastMsg = "Please enter valid email"
                }
                else -> {
                    forgotPassword(editText.text.toString())
                    toastMsg = "Please check your inbox"
                    dialog.dismiss()
                }

            }
            Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
        }
        noBtn.setOnClickListener {

            Toast.makeText(context, "Canceled ", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun forgotPassword(email: String) {
        FireBaseRepo.forgotPassword(email)
    }


    private var _navigateToRegister = MutableLiveData<Boolean>()
    val navigateToRegister: LiveData<Boolean>
        get() = _navigateToRegister

    fun RegisterNow() {
        _navigateToRegister.value = true
    }

    fun RegisterNavigationComplete() {
        _navigateToRegister.value = false
    }
}

enum class LogInStateState {
    EmailNotValid,
    PasswordNotValid,
    EmailOrPasswordIsNotCorrect,
    EmailIsNotVerified,
    FAiledToLogIn,
    LoggedIn
}