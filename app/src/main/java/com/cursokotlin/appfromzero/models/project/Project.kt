package com.cursokotlin.appfromzero.models.project

data class Project(
    val id: Long,
    val name: String,
    val description: String,
    val state: String,
    val progress: Int,
    val ownerId: Long,
    val developerId: Long?,
    val candidatesList: List<Candidate>, // Adjust the type if you have a specific class for candidates
    val languages: List<Language>,
    val frameworks: List<Framework>,
    val type: String,
    val budget: String,
    val methodologies: String
)
