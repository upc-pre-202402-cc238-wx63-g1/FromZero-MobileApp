package com.cursokotlin.appfromzero.data.remote.project

import com.cursokotlin.appfromzero.models.project.Project
import com.cursokotlin.appfromzero.models.project.ProjectProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
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
}