package com.cursokotlin.appfromzero.data.remote.authentication

import com.cursokotlin.appfromzero.models.authentication.AuthenticationRequest
import com.cursokotlin.appfromzero.models.authentication.AuthenticationResponse
import com.cursokotlin.appfromzero.models.authentication.DeveloperRegisterRequest
import com.cursokotlin.appfromzero.models.authentication.EnterpriseRegisterRequest
import com.cursokotlin.appfromzero.models.authentication.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {
    @POST("authentication/sign-in")
    fun signIn(@Body request: AuthenticationRequest): Call<AuthenticationResponse>

    @POST("authentication/sign-up/developer")
    fun registerDeveloper(@Body request: DeveloperRegisterRequest): Call<RegisterResponse>

    @POST("authentication/sign-up/enterprise")
    fun registerEnterprise(@Body request: EnterpriseRegisterRequest): Call<RegisterResponse>
}
