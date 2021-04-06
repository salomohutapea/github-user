package com.example.githubuser.handlers

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.adapters.UserAdapter
import com.example.githubuser.models.Users
import com.example.githubuser.viewmodels.UserListViewModel
import com.example.githubuser.views.DetailActivity

class RecyclerViewHandler {

    private var currentPosition = 0
    var list = ArrayList<Users>()

    fun showRecyclerView(
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
            Log.d("NOTIFY1ITEM", list.toString())
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
}