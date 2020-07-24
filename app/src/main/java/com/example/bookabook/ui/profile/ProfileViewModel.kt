package com.example.bookabook.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookabook.data.FireBaseRepo
import com.example.bookabook.data.UserCallBack

class ProfileViewModel : ViewModel() {

    var userName = MutableLiveData<String>()

    var email = MutableLiveData<String>()

     fun getUserNow() {
        FireBaseRepo.getUser(UserCallBack {
            val user = it
            userName.value = user.userName
            email.value = user.email
        })
    }

    var logOutbtn = MutableLiveData<Boolean>()

    fun signOut() {
        logOutbtn.value = true
    }
    fun completeLogOut()
    {
        logOutbtn.value = false
    }
}