package com.example.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuser.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "favDb"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " ( ${DatabaseContract.FavoriteColumns._USERNAME} TEXT PRIMARY KEY NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.name} TEXT," +
                " ${DatabaseContract.FavoriteColumns.company} TEXT," +
                " ${DatabaseContract.FavoriteColumns.location} TEXT," +
                " ${DatabaseContract.FavoriteColumns.followers} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.following} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.public_repos} TEXT NOT NULL," +
                " ${DatabaseContract.FavoriteColumns.avatar} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}