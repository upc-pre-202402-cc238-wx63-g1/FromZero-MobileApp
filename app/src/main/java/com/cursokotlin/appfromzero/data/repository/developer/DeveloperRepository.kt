package com.cursokotlin.appfromzero.data.repository.developer

import com.cursokotlin.appfromzero.data.remote.developer.DeveloperService
import com.cursokotlin.appfromzero.models.profile.DeveloperProfileResponse
import com.cursokotlin.appfromzero.models.profile.UpdateDeveloperProfileRequest
import retrofit2.Call

class DeveloperRepository (private val developerService: DeveloperService) {
    fun getDeveloperByUserId(userId: Long, token: String): Call<DeveloperProfileResponse>{
        return developerService.getDeveloperByUserId(userId, "Bearer $token")
    }

    fun updateDeveloperProfile(id: Long, updateDeveloperProfileRequest: UpdateDeveloperProfileRequest, token: String): Call<DeveloperProfileResponse>{
        return developerService.updateDeveloperProfile(id, updateDeveloperProfileRequest, "Bearer $token")
    }
}