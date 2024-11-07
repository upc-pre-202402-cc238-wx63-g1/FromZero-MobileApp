package com.cursokotlin.appfromzero.data.remote.comunication

import com.cursokotlin.appfromzero.models.comunication.MessageRequest
import com.cursokotlin.appfromzero.models.comunication.MessageResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call


interface MessageService {

    @POST("messages")
    fun createMessage(@Body request: MessageRequest): Call<MessageResponse>
}