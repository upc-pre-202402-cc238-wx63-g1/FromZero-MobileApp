package com.cursokotlin.appfromzero.models

import com.cursokotlin.appfromzero.models.project.Candidate

class ProjectCard (
    val idProject: Long,
    val projectName: String,
    val numPostulantes: Int,
    val enterpriseName: String,
    val pictureUrl: String,
    val projectState: ProjectState,
    val projectProgress: Int,
    val candidateList: List<Candidate>
)

enum class ProjectState {
    BUSQUEDA_DEVELOPER,
    EN_PROGRESO,
    FINALIZADO
}