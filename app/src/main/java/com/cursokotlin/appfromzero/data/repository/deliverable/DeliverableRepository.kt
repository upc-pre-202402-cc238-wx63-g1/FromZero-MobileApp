package com.cursokotlin.appfromzero.data.repository.deliverable

import com.cursokotlin.appfromzero.data.remote.deliverable.DeliverableService
import com.cursokotlin.appfromzero.models.Deliverable
import com.cursokotlin.appfromzero.models.deliverable.DeliverableResponse
import retrofit2.Call

class DeliverableRepository (private val deliverableService: DeliverableService) {

    fun getDeliverablesByProjectId(projectId: Long, token: String): Call<List<Deliverable>> {
        return deliverableService.getDeliverablesByProjectId(projectId, "Bearer $token")
    }

    fun createDeliverable(project: DeliverableResponse, token: String): Call<DeliverableResponse> {
        return deliverableService.createDeliverable(project, "Bearer $token")
    }
}