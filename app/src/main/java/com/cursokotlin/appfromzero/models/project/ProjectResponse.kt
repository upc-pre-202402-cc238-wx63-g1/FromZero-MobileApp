package com.cursokotlin.appfromzero.models.project

data class ProjectResponse (
    val name: String,
    val description: String,
    val ownerId: Long,
    val languages: List<String>? =  null,
    val frameworks: List<String>? =  null,
    val type: String? = null,
    val budget: String? =  null,
    val methodologies: String? =  null,
)