package com.cursokotlin.appfromzero.models.comunication

data class MessageResponse (
    val id: Long,
    val subject: String,
    val emailBody: String,
    val recipientId: Long,
    val senderId: Long,
    val sentTime: String
)