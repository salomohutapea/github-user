package com.example.githubuser.helpers

import android.content.Context
import android.content.Intent
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.adapters.FavoriteAdapter
import com.example.githubuser.adapters.UserAdapter
import com.example.githubuser.models.UserDetail
import com.example.githubuser.models.Users
import com.example.githubuser.viewmodels.FavoriteViewModel
import com.example.githubuser.viewmodels.UserListViewModel
import com.example.githubuser.views.DetailActivity

class ListHandler {

    private var currentPosition = 0
    var list = ArrayList<Users>()

    fun showUserRecyclerView(
        recyclerView: RecyclerView,
        viewModel: UserListViewModel,
        owner: LifecycleOwner,
        context: Context,
        token: String
    ) {
        currentPosition = 0
        val listUserAdapter = UserAdapter(list)
        recyclerView.adapter = listUserAdapter
        recyclerView.itemAnimator = null
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.setDetailUser(list, token)

        viewModel.getDetailResult().second.observe(owner) {
            currentPosition = it
        }

        viewModel.getDetailResult().first.observe(owner) {
            it.forEachIndexed { i, user ->
                list[i].followers = user.followers
                list[i].following = user.following
                list[i].public_repos = user.public_repos
            }
            listUserAdapter.notifyItemChanged(currentPosition)
        }

        listUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("USERNAME", data.username)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        })
    }

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
            override fun onItemClicked(username: String, position: Int) {
                favoriteViewModel.deleteFavorite(context, username, position)
            }
        })
        listFavAdapter.setOnDetailCallback(object : FavoriteAdapter.OnDetailClickCallback {
            override fun onDetailFavClicked(username: String) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("USERNAME", username)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        })

        favoriteViewModel.getStatus().observe(owner) {
            favorites.removeAt(it)
            listFavAdapter.notifyDataSetChanged()
            Toast.makeText(context, context.getText(R.string.deleted_fav), Toast.LENGTH_SHORT).show()
        }
    }

}