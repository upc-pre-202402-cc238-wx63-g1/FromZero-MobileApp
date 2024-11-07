package com.cursokotlin.appfromzero.models.project

data class Candidate(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val description: String,
    val country: String,
    val phone: String,
    val completedProjects: Int,
    val specialties: String,
    val profileImgUrl: String,
    val userId: Long
)