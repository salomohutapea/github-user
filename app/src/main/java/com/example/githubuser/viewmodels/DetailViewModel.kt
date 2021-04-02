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

    fun setDetailUser(username: String) {
        NetworkHandler().getService().getDetailUser(username).enqueue(object :
            Callback<UserDetail> {

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                Log.d("Request Failed", "Search user")
            }

            override fun onResponse(
                call: Call<UserDetail>,
                response: Response<UserDetail>
            ) {

                if (response.code() in 400..598) {
                    Log.d("${response.code()} Error", response.toString());
                }

                detailResult.postValue(response.body())
            }
        })
    }

    fun setFollowers(username: String) {
        val data = ArrayList<Users>()
        NetworkHandler().getService().getFollowers(username).enqueue(object :
            Callback<ArrayList<Users>> {

            override fun onFailure(call: Call<ArrayList<Users>>, t: Throwable) {
                Log.d("Request Failed", "Get followers")
            }

            override fun onResponse(
                call: Call<ArrayList<Users>>,
                response: Response<ArrayList<Users>>
            ) {

                if (response.code() in 400..598) {
                    Log.d("${response.code()} Error", response.toString());
                }

                response.body()?.forEach() {

                    if (it.avatar != null && it.username != null) {
                        data.add(it)
                    }
                }
                detailFollowers.postValue(data)
            }
        })
    }

    fun setFollowing(username: String) {
        val data = ArrayList<Users>()
        NetworkHandler().getService().getFollowing(username).enqueue(object :
            Callback<ArrayList<Users>> {

            override fun onFailure(call: Call<ArrayList<Users>>, t: Throwable) {
                Log.d("Request Failed", "Get following")
            }

            override fun onResponse(
                call: Call<ArrayList<Users>>,
                response: Response<ArrayList<Users>>
            ) {

                if (response.code() in 400..598) {
                    Log.d("${response.code()} Error", response.toString());
                }

                response.body()?.forEach() {

                    if (it.avatar != null && it.username != null) {
                        data.add(it)
                    }
                }
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
}