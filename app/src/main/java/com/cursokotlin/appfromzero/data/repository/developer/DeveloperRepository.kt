package com.cursokotlin.appfromzero.data.repository.developer

import com.cursokotlin.appfromzero.data.remote.developer.DeveloperService
import com.cursokotlin.appfromzero.models.profile.DeveloperProfileResponse
import retrofit2.Call

class DeveloperRepository (private val developerService: DeveloperService) {
    fun getDeveloperByUserId(userId: Long, token: String): Call<DeveloperProfileResponse>{
        return developerService.getDeveloperByUserId(userId, "Bearer $token")
    }
}