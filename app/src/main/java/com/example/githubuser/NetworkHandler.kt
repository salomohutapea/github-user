package com.example.githubuser

import com.example.githubuser.models.SearchResponse
import com.example.githubuser.models.UserDetail
import com.example.githubuser.models.Users
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

class NetworkHandler {
    // set interceptor
    private fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): ServiceNewsCall = getRetrofit().create(ServiceNewsCall::class.java)
}

interface ServiceNewsCall {
    @Headers("Authorization: token 99709aeb7b5c28e2ae0eea4dc653e884f0972822")
    @GET("search/users")
    fun searchUser(@Query("q") username: String): Call<SearchResponse>

    @Headers("Authorization: token 99709aeb7b5c28e2ae0eea4dc653e884f0972822")
    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<UserDetail>

    @Headers("Authorization: token 99709aeb7b5c28e2ae0eea4dc653e884f0972822")
    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<Users>>

    @Headers("Authorization: token 99709aeb7b5c28e2ae0eea4dc653e884f0972822")
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<Users>>

}