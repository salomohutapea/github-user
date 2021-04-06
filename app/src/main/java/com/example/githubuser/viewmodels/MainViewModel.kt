package com.example.githubuser.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.handlers.NetworkHandler
import com.example.githubuser.models.SearchResponse
import com.example.githubuser.models.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel() : ViewModel() {

    private val searchResult = MutableLiveData<ArrayList<Users>>()
    private val status = MutableLiveData<Int>()

    fun setSearchUser(username: String, token: String) {
        val data = ArrayList<Users>()
        NetworkHandler().getService(token).searchUser(username).enqueue(object :
            Callback<SearchResponse> {

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("Request Failed", "Search user")
                status.postValue(444)
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                status.postValue(response.code())
                response.body()?.items?.forEach {
                    if (it.avatar != null && it.username != null) {
                        data.add(it)
                    }
                }
                searchResult.postValue(data)
            }
        })
    }

    // Overloading method
    fun setSearchUser(username: String, token: String, page: String) {
        val data = ArrayList<Users>()
        NetworkHandler().getService(token).searchUserPaged(username, page).enqueue(object :
            Callback<SearchResponse> {

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("Request Failed", "Search user")
                status.postValue(444)
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                status.postValue(response.code())
                response.body()?.items?.forEach {
                    if (it.avatar != null && it.username != null) {
                        data.add(it)
                    }
                }
                searchResult.postValue(data)
            }
        })
    }

    fun getSearchResult(): LiveData<ArrayList<Users>> {
        return searchResult
    }

    fun getStatus(): LiveData<Int> {
        return status
    }
}