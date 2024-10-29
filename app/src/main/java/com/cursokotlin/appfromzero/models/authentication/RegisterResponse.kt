package com.cursokotlin.appfromzero.models.authentication

data class RegisterResponse(
    val id : Long,
    val username : String,
    val roles : List<String>
)
