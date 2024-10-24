package com.cursokotlin.appfromzero.models

class ProjectCard (
    val projectName: String,
    val numPostulantes: Int,
    val enterpriseName: String,
    val pictureUrl: String,
    val projectState: ProjectState,
    val projectProgress: Int
)

enum class ProjectState {
    BUSQUEDA_DEVELOPER,
    EN_PROGRESO,
    FINALIZADO
}