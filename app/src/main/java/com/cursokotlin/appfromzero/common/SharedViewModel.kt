package com.cursokotlin.appfromzero.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cursokotlin.appfromzero.models.project.Candidate
import com.cursokotlin.appfromzero.models.project.Project
import retrofit2.Call
import retrofit2.Response

class SharedViewModel : ViewModel() {
    private val _acceptedCandidate = MutableLiveData<Candidate>()
    val acceptedCandidate: LiveData<Candidate> get() = _acceptedCandidate

    fun setAcceptedCandidate(candidate: Candidate) {
        _acceptedCandidate.value = candidate
    }
}