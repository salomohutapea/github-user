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

class UserListViewModel : ViewModel() {

    private val detailResult = MutableLiveData<ArrayList<UserDetail>>()
    private val currentPosition = MutableLiveData<Int>()
    private val status = MutableLiveData<Int>()
    private val isLoading = MutableLiveData<Boolean>()

    fun setDetailUser(users: ArrayList<Users>, token: String) {
        val listDetail = ArrayList<UserDetail>()
        users.forEachIndexed { i, user ->
            user.username?.let {
                NetworkHandler().getService(token).getDetailUser(it).enqueue(object :
                    Callback<UserDetail> {

                    override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                        Log.d("Request Failed", "Search user")
                        status.postValue(444)
                    }

                    override fun onResponse(
                        call: Call<UserDetail>,
                        response: Response<UserDetail>
                    ) {
                        response.body()?.let { it1 -> listDetail.add(it1) }
                        status.postValue(response.code())
                        currentPosition.postValue(i)
                        detailResult.postValue(listDetail)
                        if (listDetail.size == users.size) {
                            isLoading.postValue(false)
                        }
                    }
                })
            }
        }
    }

    fun getLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getDetailResult(): Pair<LiveData<ArrayList<UserDetail>>, LiveData<Int>> {
        return Pair(detailResult, currentPosition)
    }

    fun getStatus(): LiveData<Int> {
        return status
    }

}
