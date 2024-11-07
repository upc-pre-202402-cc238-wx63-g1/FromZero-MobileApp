package com.cursokotlin.appfromzero.data.repository.comunication

import com.cursokotlin.appfromzero.data.remote.comunication.MessageService
import com.cursokotlin.appfromzero.models.comunication.MessageRequest
import com.cursokotlin.appfromzero.models.comunication.MessageResponse
import retrofit2.Call

class MessageRepository (private val messageService: MessageService) {

    fun createMessage(request: MessageRequest): Call<MessageResponse> {
        return messageService.createMessage(request)
    }

}