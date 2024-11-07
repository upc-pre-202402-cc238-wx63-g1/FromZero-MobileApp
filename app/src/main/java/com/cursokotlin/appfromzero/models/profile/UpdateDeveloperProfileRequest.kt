package com.cursokotlin.appfromzero.models.profile

data class UpdateDeveloperProfileRequest(
    val firstName: String,
    val lastName: String,
    val description: String,
    val country: String,
    val phone: String,
    val specialties: String,
    val profileImgUrl: String
)
