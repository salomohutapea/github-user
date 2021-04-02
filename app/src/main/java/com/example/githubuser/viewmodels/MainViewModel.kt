package com.example.githubuser.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.NetworkHandler
import com.example.githubuser.models.SearchResponse
import com.example.githubuser.models.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel() : ViewModel() {

    private val searchResult = MutableLiveData<ArrayList<Users>>()

    fun setSearchUser(username: String) {
        val data = ArrayList<Users>()
        NetworkHandler().getService().searchUser(username).enqueue(object :
            Callback<SearchResponse> {

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("Request Failed", "Search user")
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                if (response.code() in 400..598) {
                    Log.d("${response.code()} Error", response.toString())
                }

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
}