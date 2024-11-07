package com.cursokotlin.appfromzero.models.deliverable

data class UpdateDeliverableResponse (
    val id: Long,
    val name: String,
    val description: String,
    val date: String,
    val state: String,
    val developerMessage: String,
    val projectId: Long
)


