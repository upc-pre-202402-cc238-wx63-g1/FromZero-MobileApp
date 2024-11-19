package com.cursokotlin.appfromzero.data.repository.comunication

import com.cursokotlin.appfromzero.data.remote.comunication.SupportService
import com.cursokotlin.appfromzero.models.comunication.SupportRequest
import com.cursokotlin.appfromzero.models.comunication.SupportResponse
import retrofit2.Call

class SupportRepository(private val supportService: SupportService) {

    fun createSupport(request: SupportRequest): Call<SupportResponse> {
        return supportService.createSupport(request)
    }

}