package com.cursokotlin.appfromzero.data.remote.comunication

import retrofit2.Call
import com.cursokotlin.appfromzero.models.comunication.SupportRequest
import com.cursokotlin.appfromzero.models.comunication.SupportResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SupportService {

    @POST("support-tickets")
    fun createSupport(@Body request: SupportRequest): Call<SupportResponse>
}