package com.cursokotlin.appfromzero.models.authentication

data class AuthenticationResponse(
    val id: Long,
    val username: String,
    val token: String,
    val roles: List<String>
)