package com.cursokotlin.appfromzero.data.repository.deliverable

import com.cursokotlin.appfromzero.data.remote.deliverable.DeliverableService
import com.cursokotlin.appfromzero.models.Deliverable
import retrofit2.Call

class DeliverableRepository (private val deliverableService: DeliverableService) {

    fun getDeliverablesByProjectId(projectId: Long, token: String): Call<List<Deliverable>> {
        return deliverableService.getDeliverablesByProjectId(projectId, "Bearer $token")
    }
}