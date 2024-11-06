package com.cursokotlin.appfromzero.models.profile

data class DeveloperProfileResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val description: String,
    val country: String,
    val phone: String,
    val specialties: String,
    val profileImgUrl: String,
    val userId: Long
)