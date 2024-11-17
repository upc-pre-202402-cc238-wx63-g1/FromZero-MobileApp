package com.cursokotlin.appfromzero.data.repository.enterprise

import retrofit2.Call
import com.cursokotlin.appfromzero.data.remote.enterprise.EnterpriseService
import com.cursokotlin.appfromzero.models.profile.EnterpriseProfileResponse
import com.cursokotlin.appfromzero.models.profile.UpdateEnterpriseProfileRequest

class EnterpriseRepository (private val enterpriseService: EnterpriseService) {
    fun getDeveloperByUserId(userId: Long, token: String): Call<EnterpriseProfileResponse>{
        return enterpriseService.getDeveloperByUserId(userId, "Bearer $token")
    }

    fun updateEnterpriseProfile(id: Long, updateEnterpriseProfileRequest: UpdateEnterpriseProfileRequest, token: String): Call<EnterpriseProfileResponse>{
        return enterpriseService.updateEnterpriseByUserId(id, updateEnterpriseProfileRequest, "Bearer $token")
    }
}