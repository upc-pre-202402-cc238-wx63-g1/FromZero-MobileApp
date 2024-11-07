package com.cursokotlin.appfromzero.models.profile

data class DeveloperSearchCard(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val rating: Float,
    val description: String,
    val country: String,
    val specialties: String,
    val profileImgUrl: String,
    val userId: Long
)
