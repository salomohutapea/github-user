package com.example.consumerapp.handlers

import android.content.Context
import android.util.Log
import android.widget.ListView
import androidx.lifecycle.LifecycleOwner
import com.example.consumerapp.adapter.FavoriteAdapter
import com.example.consumerapp.models.UserDetail
import com.example.consumerapp.viewmodels.FavoriteViewModel

class ListHandler {

    fun showFavoriteListView(
        listView: ListView,
        favoriteViewModel: FavoriteViewModel,
        favorites: ArrayList<UserDetail>,
        context: Context,
        owner: LifecycleOwner
    ) {
        val listFavAdapter = FavoriteAdapter(favorites)
        listView.adapter = listFavAdapter

        listFavAdapter.setOnDeleteCallback(object : FavoriteAdapter.OnDeleteClickCallback {
            override fun onItemClicked(id: String, position: Int) {
                Log.d("DCLICK", "hi")
                favoriteViewModel.deleteItem(context, id)
            }
        })
    }

}