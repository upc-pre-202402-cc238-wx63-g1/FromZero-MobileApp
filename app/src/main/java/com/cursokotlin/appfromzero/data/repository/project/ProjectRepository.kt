package com.cursokotlin.appfromzero.data.repository.project

import com.cursokotlin.appfromzero.data.remote.project.ProjectService
import com.cursokotlin.appfromzero.models.project.Project
import com.cursokotlin.appfromzero.models.project.ProjectProfileResponse
import retrofit2.Call

class ProjectRepository (private val projectService: ProjectService) {
    fun getProjectsByEnterpriseUserId(userId: Long, token: String): Call<List<Project>> {
        return projectService.getProjectsByUserEnterpriseId(userId, "Bearer $token")
    }

    fun getProjectsByDeveloperUserId(userId: Long, token: String): Call<List<Project>> {
        return projectService.getProjectsByUserDeveloperId(userId, "Bearer $token")
    }

    fun getProjectById(projectId: Long, token: String): Call<Project> {
        return projectService.getProjectById(projectId, "Bearer $token")
    }
}