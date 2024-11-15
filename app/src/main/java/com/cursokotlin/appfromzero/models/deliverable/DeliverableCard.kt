package com.cursokotlin.appfromzero.models.deliverable

import java.time.LocalDate

data class DeliverableCard (
    val id: Long,
    val name:String,
    val description: String,
    val date: String,
    val state: String,
    val projectId: Long,
    val developerMessage: String,
    var projectName:String
)