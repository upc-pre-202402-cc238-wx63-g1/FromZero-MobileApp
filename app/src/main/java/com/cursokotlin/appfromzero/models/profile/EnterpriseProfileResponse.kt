package com.cursokotlin.appfromzero.models.profile

data class EnterpriseProfileResponse(
    val id: Long,
    val enterpriseName: String,
    val description: String,
    val country: String,
    val ruc: String,
    val phone: String,
    val website: String,
    val profileImgUrl: String,
    val sector: String,
    val userId: Long
)
