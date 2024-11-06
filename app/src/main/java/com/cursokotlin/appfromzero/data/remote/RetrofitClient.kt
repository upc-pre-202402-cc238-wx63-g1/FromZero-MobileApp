package com.cursokotlin.appfromzero.data.remote

import com.cursokotlin.appfromzero.common.Constants
import com.cursokotlin.appfromzero.data.remote.authentication.AuthenticationService
import com.cursokotlin.appfromzero.data.remote.developer.DeveloperService
import com.cursokotlin.appfromzero.data.remote.enterprise.EnterpriseService
import com.cursokotlin.appfromzero.data.remote.project.ProjectService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

object RetrofitClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getClient(token: String): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authenticationService: AuthenticationService by lazy {
        retrofit.create(AuthenticationService::class.java)
    }

    val enterpriseService: EnterpriseService by lazy {
        retrofit.create(EnterpriseService::class.java)
    }

    val projectService: ProjectService by lazy {
        retrofit.create(ProjectService::class.java)
    }

    val developerService: DeveloperService by lazy{
        retrofit.create(DeveloperService::class.java)
    }

}

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}