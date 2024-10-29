package com.cursokotlin.appfromzero.data.repository.authentication

import retrofit2.Call
import com.cursokotlin.appfromzero.data.remote.authentication.AuthenticationService
import com.cursokotlin.appfromzero.models.authentication.AuthenticationRequest
import com.cursokotlin.appfromzero.models.authentication.AuthenticationResponse
import com.cursokotlin.appfromzero.models.authentication.DeveloperRegisterRequest
import com.cursokotlin.appfromzero.models.authentication.EnterpriseRegisterRequest
import com.cursokotlin.appfromzero.models.authentication.RegisterResponse

class AuthenticationRepository(private val authenticationService: AuthenticationService) {

    fun signIn(request: AuthenticationRequest): Call<AuthenticationResponse> {
        return authenticationService.signIn(request)
    }

    fun registerDeveloper(request: DeveloperRegisterRequest): Call<RegisterResponse> {
        return authenticationService.registerDeveloper(request)
    }

    fun registerEnterprise(request: EnterpriseRegisterRequest): Call<RegisterResponse> {
        return authenticationService.registerEnterprise(request)
    }
}