package com.cursokotlin.appfromzero.models.authentication

data class DeveloperRegisterRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String
)