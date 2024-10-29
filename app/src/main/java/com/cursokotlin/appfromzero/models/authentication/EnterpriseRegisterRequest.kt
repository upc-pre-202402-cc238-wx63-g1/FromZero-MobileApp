package com.cursokotlin.appfromzero.models.authentication

data class EnterpriseRegisterRequest(
    val username: String,
    val password: String,
    val enterpriseName: String
)