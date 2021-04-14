package com.example.consumerapp.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumerapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.consumerapp.db.MappingHelper
import com.example.consumerapp.models.UserDetail

class FavoriteViewModel : ViewModel() {
    private val dbReadResult = MutableLiveData<ArrayList<UserDetail>>()

    fun loadFav(context: Context) {
        val cursor = context.contentResolver.query(
            CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val list = MappingHelper.mapCursorToArrayList(cursor)
        dbReadResult.postValue(list)
    }

    fun getDbReadResult(): LiveData<ArrayList<UserDetail>> {
        return dbReadResult
    }

}