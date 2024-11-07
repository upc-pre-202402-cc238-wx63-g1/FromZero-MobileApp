package com.cursokotlin.appfromzero.data.remote.project

import com.cursokotlin.appfromzero.models.project.Project
import com.cursokotlin.appfromzero.models.project.ProjectProfileResponse

import com.cursokotlin.appfromzero.models.project.ProjectResponse

import com.cursokotlin.appfromzero.models.project.ProjectSearchCard

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


interface ProjectService {
    @GET("projects/enterprise/{id}")
    fun getProjectsByUserEnterpriseId(
        @Path("id") enterpriseId: Long,
        @Header("Authorization") token: String
    ): Call<List<Project>>

    @GET("projects/developer/{id}")
    fun getProjectsByUserDeveloperId(
        @Path("id") developerId: Long,
        @Header("Authorization") token: String
    ): Call<List<Project>>

    @GET("projects/{id}")
    fun getProjectById(
        @Path("id") projectId: Long,
        @Header("Authorization") token: String
    ): Call<Project>


    @POST("projects")
    fun createProject(
        @Body project: ProjectResponse,
        @Header("Authorization") token: String
    ): Call<ProjectResponse>

    @GET("projects")
    fun getProjects(
        @Header("Authorization") token: String
    ): Call<List<ProjectSearchCard>>

    @PATCH("projects/{projectId}/assign-developer")
    fun assignDeveloper(
        @Path("projectId") projectId: Long,
        @Body developerId: Long,
        @Header("Authorization") token: String
    ): Call<Project>

}