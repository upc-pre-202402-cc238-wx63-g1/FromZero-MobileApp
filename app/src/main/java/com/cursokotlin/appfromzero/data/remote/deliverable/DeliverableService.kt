package com.cursokotlin.appfromzero.data.remote.deliverable

import com.cursokotlin.appfromzero.models.Deliverable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DeliverableService {
    @GET("deliverables/project/{id}")
    fun getDeliverablesByProjectId(
        @Path("id") projectId: Long,
        @Header("Authorization") token: String
    ): Call<List<Deliverable>>
}