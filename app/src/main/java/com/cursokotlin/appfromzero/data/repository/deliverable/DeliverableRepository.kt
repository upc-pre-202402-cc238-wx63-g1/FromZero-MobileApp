package com.cursokotlin.appfromzero.data.repository.deliverable

import com.cursokotlin.appfromzero.data.remote.deliverable.DeliverableService
import com.cursokotlin.appfromzero.models.Deliverable
import com.cursokotlin.appfromzero.models.deliverable.DeliverableResponse
import com.cursokotlin.appfromzero.models.deliverable.UpdateDeliverableRequest
import com.cursokotlin.appfromzero.models.deliverable.UpdateDeliverableResponse
import com.cursokotlin.appfromzero.models.profile.DeveloperProfileResponse
import com.cursokotlin.appfromzero.models.profile.UpdateDeveloperProfileRequest
import retrofit2.Call
import retrofit2.Response

class DeliverableRepository (private val deliverableService: DeliverableService) {

    fun getDeliverablesByProjectId(projectId: Long, token: String): Call<List<Deliverable>> {
        return deliverableService.getDeliverablesByProjectId(projectId, "Bearer $token")
    }

    fun createDeliverable(project: DeliverableResponse, token: String): Call<DeliverableResponse> {
        return deliverableService.createDeliverable(project, "Bearer $token")
    }

    fun updateDeliverable(id: Long, updateDeliverableRequest: UpdateDeliverableRequest, token: String): Call<UpdateDeliverableResponse> {
        return deliverableService.updateDeliverable(id, updateDeliverableRequest, "Bearer $token")
    }

    fun deleteDeliverable(deliverableId: Long, token: String): Call<Void> {
        return deliverableService.deleteDeliverable(deliverableId, "Bearer $token")
    }
}