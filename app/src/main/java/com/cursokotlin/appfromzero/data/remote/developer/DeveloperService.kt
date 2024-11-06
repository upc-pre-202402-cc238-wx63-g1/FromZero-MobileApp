package com.cursokotlin.appfromzero.data.remote.developer

import com.cursokotlin.appfromzero.models.profile.DeveloperProfileResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DeveloperService {
    @GET("developers/user/{id}")
    fun getDeveloperByUserId(
        @Path("id") userId: Long,
        @Header("Authorization") token: String
    ): Call<DeveloperProfileResponse>
}