package com.example.githubuser.helpers

import com.example.githubuser.models.SearchResponse
import com.example.githubuser.models.UserDetail
import com.example.githubuser.models.Users
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class NetworkHandler {

    // set interceptor
    private fun getInterceptor(token: String): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", token).build()
                chain.proceed(request)
            }
            .build()
    }

    private fun getRetrofit(token: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(getInterceptor(token))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(token: String): ServiceApiCall = getRetrofit(token).create(ServiceApiCall::class.java)
}

interface ServiceApiCall {
    @GET("search/users")
    fun searchUser(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<UserDetail>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<Users>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<Users>>
}