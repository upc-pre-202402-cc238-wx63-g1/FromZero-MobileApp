package com.cursokotlin.appfromzero.data.repository.project

import com.cursokotlin.appfromzero.data.remote.project.ProjectService
import com.cursokotlin.appfromzero.models.project.Project
import com.cursokotlin.appfromzero.models.project.ProjectProfileResponse

import com.cursokotlin.appfromzero.models.project.ProjectResponse

import com.cursokotlin.appfromzero.models.project.ProjectSearchCard

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

    fun createProject(project: ProjectResponse, token: String): Call<ProjectResponse> {
        return projectService.createProject(project, "Bearer $token")
    }
    
    fun getProjectsByState(state: String,token: String): Call<List<ProjectSearchCard>>{
        return projectService.getProjectsByState(state,"Bearer $token")
    }

    fun assignDeveloper(projectId: Long, developerId: Long, token: String): Call<Project> {
        return projectService.assignDeveloper(projectId, developerId, "Bearer $token")
    }
}