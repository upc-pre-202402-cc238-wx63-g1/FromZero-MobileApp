package com.cursokotlin.appfromzero.models.comunication

data class MessageRequest (
    val subject : String,
    val emailBody : String,
    val recipientId : Long,
    val senderId : Long
)