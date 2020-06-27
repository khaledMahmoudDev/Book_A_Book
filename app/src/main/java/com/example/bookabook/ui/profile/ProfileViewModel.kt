package com.example.bookabook.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    var logOutbtn = MutableLiveData<Boolean>()

    fun signOut() {
        logOutbtn.value = true
    }
    fun completeLogOut()
    {
        logOutbtn.value = false
    }
}