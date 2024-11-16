package com.cursokotlin.appfromzero.models

data class Developer(
    val name: String,
    val rating: Float,
    val profilePic: Int,
    val countryFlag: Int,
    val summary: String,
    val skills: String,
    val phone: String? = "999 999 999",
    val email: String? = "example@gmail.com",
    val profileImgUrl: String? = "https://i.pinimg.com/474x/c5/43/cd/c543cd798d203442a63ee559dd3e0d7f.jpg"
)