package com.cursokotlin.appfromzero.data.remote.enterprise

import com.cursokotlin.appfromzero.models.profile.EnterpriseProfileResponse
import com.cursokotlin.appfromzero.models.profile.UpdateEnterpriseProfileRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface EnterpriseService {
    @GET("enterprises/user/{id}")
    fun getDeveloperByUserId(
        @Path("id") userId: Long,
        @Header("Authorization") token: String
    ): Call<EnterpriseProfileResponse>

    @PUT("enterprises/{id}")
    fun updateEnterpriseByUserId(
        @Path("id") id: Long,
        @Body updateEnterpriseProfileRequest: UpdateEnterpriseProfileRequest,
        @Header("Authorization") token: String
    ): Call<EnterpriseProfileResponse>
}