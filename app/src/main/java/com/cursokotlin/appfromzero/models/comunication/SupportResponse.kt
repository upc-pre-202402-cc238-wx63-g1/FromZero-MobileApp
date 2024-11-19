package com.cursokotlin.appfromzero.models.comunication

data class SupportResponse (
    val id: Long,
    val title: String,
    val type: String,
    val description: String,
    val senderId: Long,
    val sentTime: String
)