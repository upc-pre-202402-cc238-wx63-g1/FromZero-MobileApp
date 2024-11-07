package com.cursokotlin.appfromzero.models.project

data class ProjectSearchCard(
    val id: Long,
    val name: String,
    val description: String,
    val pictureLogo: Int? = 0,
    val website: String
)