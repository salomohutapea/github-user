package com.example.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavoriteColumns : BaseColumns {
        companion object {
            private const val AUTHORITY = "com.example.githubuser"
            private const val SCHEME = "content"

            const val TABLE_NAME = "favorite"
            const val _USERNAME = "username"
            const val name = "name"
            const val following = "following"
            const val followers = "followers"
            const val public_repos = "public_repos"
            const val company = "company"
            const val location = "location"
            const val avatar = "avatar_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}