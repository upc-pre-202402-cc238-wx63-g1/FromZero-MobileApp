package com.cursokotlin.appfromzero.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val userRole = MutableLiveData<String>()
}