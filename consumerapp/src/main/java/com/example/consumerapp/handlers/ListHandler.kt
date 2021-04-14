package com.example.consumerapp.handlers

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consumerapp.adapter.FavoritesAdapter
import com.example.consumerapp.models.UserDetail

class ListHandler {

    fun showFavoriteListView(
        rv: RecyclerView,
        favorites: ArrayList<UserDetail>,
        context: Context
    ) {
        val listFavAdapter = FavoritesAdapter(favorites)
        rv.adapter = listFavAdapter
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(context)
    }

}