package com.cursokotlin.appfromzero.models.comunication

data class SupportRequest (
    val title : String,
    val type : String,
    val description : String,
    val senderId : Long
)