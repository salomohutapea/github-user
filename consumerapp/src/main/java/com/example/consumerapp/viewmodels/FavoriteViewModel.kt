package com.example.consumerapp.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumerapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.consumerapp.db.MappingHelper
import com.example.consumerapp.models.UserDetail

class FavoriteViewModel : ViewModel() {
    private val dbReadResult = MutableLiveData<ArrayList<UserDetail>>()
    private val status = MutableLiveData<Int>()

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

    //TODO: DELETE ITEM
    fun deleteItem(context: Context, id: String) {
        val uri = Uri.parse("$CONTENT_URI/$id")
        val deleted = context.contentResolver.delete(
            uri,
            null,
            null
        )
        if (deleted == 1) {
            status.postValue(222)
        } else {
            status.postValue(444)
        }
    }

    fun getDbReadResult(): LiveData<ArrayList<UserDetail>> {
        return dbReadResult
    }

    fun getStatus(): LiveData<Int> {
        return status
    }

}