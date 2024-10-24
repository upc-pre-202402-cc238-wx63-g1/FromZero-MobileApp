package com.cursokotlin.appfromzero.models

data class Developer(
    val name: String,
    val rating: Float,
    val profilePic: Int,
    val countryFlag: Int,
    val summary: String,
    val skills: String,
    val phone: String? = "999 999 999",
    val email: String? = "example@gmail.com"
)