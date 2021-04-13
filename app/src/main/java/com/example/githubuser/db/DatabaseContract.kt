package com.example.githubuser.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val username = "username"
            const val name = "name"
            const val following = "following"
            const val followers = "followers"
            const val public_repos = "public_repos"
            const val company = "company"
            const val location = "location"
            const val avatar = "avatar_url"
        }
    }
}