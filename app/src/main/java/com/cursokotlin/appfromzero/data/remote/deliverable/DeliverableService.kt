package com.cursokotlin.appfromzero.data.remote.deliverable

import com.cursokotlin.appfromzero.models.Deliverable
import com.cursokotlin.appfromzero.models.deliverable.DeliverableResponse
import com.cursokotlin.appfromzero.models.deliverable.UpdateDeliverableRequest
import com.cursokotlin.appfromzero.models.deliverable.UpdateDeliverableResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DeliverableService {
    @GET("deliverables/project/{id}")
    fun getDeliverablesByProjectId(
        @Path("id") projectId: Long,
        @Header("Authorization") token: String
    ): Call<List<Deliverable>>

    @POST("deliverables")
    fun createDeliverable(
        @Body project: DeliverableResponse,
        @Header("Authorization") token: String
    ): Call<DeliverableResponse>

    @PUT("deliverables/{deliverableId}")
    fun updateDeliverable(
        @Path("deliverableId") deliverableId: Long,
        @Body updateDeliverableRequest: UpdateDeliverableRequest,
        @Header("Authorization") token:String
    ): Call<UpdateDeliverableResponse>

    @DELETE("deliverables/{id}")
    fun deleteDeliverable(
        @Path("id") deliverableId: Long,
        @Header("Authorization") token: String
    ): Call<Void>

}