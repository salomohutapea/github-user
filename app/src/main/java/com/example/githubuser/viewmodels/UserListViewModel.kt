package com.example.githubuser.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.NetworkHandler
import com.example.githubuser.models.UserDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListViewModel : ViewModel() {

    val detailResult = MutableLiveData<UserDetail>()
    private val currentPosition = MutableLiveData<Int>()

    fun setDetailUser(username: String, position: Int) {
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

                currentPosition.postValue(position)
                detailResult.postValue(response.body())
            }
        })
    }

    fun getDetailResult(): Pair<LiveData<UserDetail>, LiveData<Int>> {
        return Pair(detailResult, currentPosition)
    }

}