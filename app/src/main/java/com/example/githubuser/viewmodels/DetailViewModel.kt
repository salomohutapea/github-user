package com.example.githubuser.viewmodels

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.R
import com.example.githubuser.db.DatabaseContract
import com.example.githubuser.db.FavoriteHelper
import com.example.githubuser.helpers.NetworkHandler
import com.example.githubuser.models.UserDetail
import com.example.githubuser.models.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val detailResult = MutableLiveData<UserDetail>()
    private val detailFollowers = MutableLiveData<ArrayList<Users>>()
    private val detailFollowing = MutableLiveData<ArrayList<Users>>()
    private val isUserFavorite = MutableLiveData<Boolean>()
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

    fun addOrDeleteFavorite(user: UserDetail, context: Context) {
        val dbHelper = FavoriteHelper(context)
        if (user.username?.let { checkIfUserExists(it, context) } != true) {
            addFavorite(dbHelper, context, user)
        } else {
            deleteFavorite(dbHelper, context, user.username)
        }
    }

    fun checkIfUserExists(username: String, context: Context): Boolean {
        val dbHelper = FavoriteHelper(context)
        dbHelper.open()
        val cursor = dbHelper.queryByUsername(username)
        return if (cursor.count == 0) {
            isUserFavorite.postValue(false)
            dbHelper.close()
            false
        } else {
            isUserFavorite.postValue(true)
            dbHelper.close()
            true
        }
    }

    private fun deleteFavorite(dbHelper: FavoriteHelper, context: Context, username: String) {
        dbHelper.open()
        username.let { dbHelper.deleteByUsername(it) }
        dbHelper.close()
        Toast.makeText(context, context.getString(R.string.deleted_fav), Toast.LENGTH_SHORT).show()
        isUserFavorite.postValue(false)
    }

    private fun addFavorite(dbHelper: FavoriteHelper, context: Context, user: UserDetail) {
        dbHelper.open()
        val values = ContentValues()
        values.put(DatabaseContract.FavoriteColumns._USERNAME, user.username)
        values.put(DatabaseContract.FavoriteColumns.name, user.name)
        values.put(DatabaseContract.FavoriteColumns.avatar, user.avatar)
        values.put(DatabaseContract.FavoriteColumns.followers, user.followers)
        values.put(DatabaseContract.FavoriteColumns.following, user.following)
        values.put(DatabaseContract.FavoriteColumns.public_repos, user.public_repos)
        if (dbHelper.insert(values) < 0) {
            Toast.makeText(context, context.getString(R.string.error_add_fav), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, context.getString(R.string.added_fav), Toast.LENGTH_SHORT).show()
        }
        isUserFavorite.postValue(true)
        dbHelper.close()
    }

    fun getDetailResult(): LiveData<UserDetail> {
        return detailResult
    }

    fun getFollowers(): LiveData<ArrayList<Users>> {
        return detailFollowers
    }

    fun getFollowing(): LiveData<ArrayList<Users>> {
        return detailFollowing
    }

    fun getIsUserFavorite(): LiveData<Boolean> {
        return isUserFavorite
    }

    fun getStatus(): LiveData<Int> {
        return status
    }
}