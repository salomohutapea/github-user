package com.example.githubuser.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.db.FavoriteHelper
import com.example.githubuser.db.MappingHelper
import com.example.githubuser.models.UserDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {
    private val dbReadResult = MutableLiveData<ArrayList<UserDetail>>()
    private val isLoading = MutableLiveData<Boolean>()
    private val status = MutableLiveData<Int>()

    fun loadFavoritesAsync(context: Context) {
        isLoading.postValue(true)

        GlobalScope.launch(Dispatchers.Main) {
            val favoriteHelper = FavoriteHelper.getInstance(context)
            favoriteHelper.open()
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFavorites.await()
            dbReadResult.postValue(favorites)
            favoriteHelper.close()
            isLoading.postValue(false)
        }

    }

    fun deleteFavorite(context: Context, id: String, position: Int) {
        val favoriteHelper = FavoriteHelper.getInstance(context)
        favoriteHelper.open()
        favoriteHelper.deleteById(id)
        favoriteHelper.close()
        status.postValue(position)
    }

    fun getDbReadResult(): LiveData<ArrayList<UserDetail>> {
        return dbReadResult
    }

    fun getStatus(): LiveData<Int> {
        return status
    }
}