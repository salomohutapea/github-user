package com.example.githubuser.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.handlers.NetworkHandler
import com.example.githubuser.models.UserDetail
import com.example.githubuser.models.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val detailResult = MutableLiveData<UserDetail>()
    private val detailFollowers = MutableLiveData<ArrayList<Users>>()
    private val detailFollowing = MutableLiveData<ArrayList<Users>>()
    private val status = MutableLiveData<Int>()

    fun setDetailUser(username: String, token: String) {
        NetworkHandler().getService(token).getDetailUser(username).enqueue(object :
            Callback<UserDetail> {

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                Log.d("Request Failed", "Search user")
                status.postValue(444)
            }

            override fun onResponse(
                call: Call<UserDetail>,
                response: Response<UserDetail>
            ) {
                status.postValue(response.code())
                detailResult.postValue(response.body())
            }
        })
    }

    fun setFollowers(username: String, token: String) {
        val data = ArrayList<Users>()
        NetworkHandler().getService(token).getFollowers(username).enqueue(object :
            Callback<ArrayList<Users>> {

            override fun onFailure(call: Call<ArrayList<Users>>, t: Throwable) {
                Log.d("Request Failed", "Get followers")
                status.postValue(444)
            }

            override fun onResponse(
                call: Call<ArrayList<Users>>,
                response: Response<ArrayList<Users>>
            ) {
                response.body()?.forEach() {

                    if (it.avatar != null && it.username != null) {
                        data.add(it)
                    }
                }
                status.postValue(response.code())
                detailFollowers.postValue(data)
            }
        })
    }

    fun setFollowing(username: String, token: String) {
        val data = ArrayList<Users>()
        NetworkHandler().getService(token).getFollowing(username).enqueue(object :
            Callback<ArrayList<Users>> {

            override fun onFailure(call: Call<ArrayList<Users>>, t: Throwable) {
                Log.d("Request Failed", "Get following")
                status.postValue(444)
            }

            override fun onResponse(
                call: Call<ArrayList<Users>>,
                response: Response<ArrayList<Users>>
            ) {
                response.body()?.forEach() {
                    if (it.avatar != null && it.username != null) {
                        data.add(it)
                    }
                }
                status.postValue(response.code())
                detailFollowing.postValue(data)
            }
        })
    }

    fun getSearchResult(): LiveData<UserDetail> {
        return detailResult
    }

    fun getFollowers(): LiveData<ArrayList<Users>> {
        return detailFollowers
    }

    fun getFollowing(): LiveData<ArrayList<Users>> {
        return detailFollowing
    }

    fun getStatus(): LiveData<Int> {
        return status
    }
}