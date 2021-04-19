package com.example.consumerapp.db

import android.database.Cursor
import com.example.consumerapp.models.UserDetail

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<UserDetail> {
        val favList = ArrayList<UserDetail>()
        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.name))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.company))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.location))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.avatar))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.followers))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.following))
                val publicrepos = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.public_repos))
                favList.add(
                    UserDetail(
                        name,
                        username,
                        location,
                        avatar,
                        followers,
                        following,
                        company,
                        publicrepos
                    )
                )
            }
        }
        return favList
    }
}