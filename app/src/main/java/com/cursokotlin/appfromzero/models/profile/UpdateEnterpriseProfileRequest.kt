package com.cursokotlin.appfromzero.models.profile

data class UpdateEnterpriseProfileRequest(
    val enterpriseName: String,
    val description: String,
    val country: String,
    val ruc: String,
    val phone: String,
    val website: String,
    val profileImgUrl: String = "https://static.vecteezy.com/system/resources/thumbnails/024/983/914/small_2x/simple-user-default-icon-free-png.png",
    val sector: String,
)
