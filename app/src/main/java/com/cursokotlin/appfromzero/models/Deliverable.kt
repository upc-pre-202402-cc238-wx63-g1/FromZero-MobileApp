package com.cursokotlin.appfromzero.models


data class Deliverable(
    var id:Long,
    var name:String,
    var description: String,
    var date:String,
    var state:String,
    var idProject:Long,
    var developerMessage:String,
    var projectName:String
)