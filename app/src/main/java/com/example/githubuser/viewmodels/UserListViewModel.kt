package com.example.githubuser.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.handlers.NetworkHandler
import com.example.githubuser.models.UserDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel : ViewModel() {

    private val detailResult = MutableLiveData<UserDetail>()
    private val currentPosition = MutableLiveData<Int>()
    private val status = MutableLiveData<Int>()

    fun setDetailUser(username: String, position: Int, token: String) {
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

                currentPosition.postValue(position)
                detailResult.postValue(response.body())
            }
        })
    }

    fun getDetailResult(): Pair<LiveData<UserDetail>, LiveData<Int>> {
        return Pair(detailResult, currentPosition)
    }

    fun getStatus(): LiveData<Int> {
        return status
    }

}