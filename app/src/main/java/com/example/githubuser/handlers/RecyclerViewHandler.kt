package com.example.githubuser.handlers

import android.content.Context
import android.content.Intent
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
    var isBounded = IntArray(100) { 0 }

    fun showRecyclerView(
        recyclerView: RecyclerView,
        viewModel: UserListViewModel,
        owner: LifecycleOwner,
        context: Context,
        token: String
    ) {
        currentPosition = 0
        isBounded = IntArray(100) { 0 }
        recyclerView.itemAnimator = null

        recyclerView.layoutManager = LinearLayoutManager(context)
        val listUserAdapter = UserAdapter(list)
        recyclerView.adapter = listUserAdapter

        viewModel.getDetailResult().second.observe(owner) {
            currentPosition = it
        }

        viewModel.getDetailResult().first.observe(owner) {
            list[currentPosition].followers = it.followers
            list[currentPosition].public_repos = it.public_repos
            list[currentPosition].following = it.following
            listUserAdapter.notifyItemChanged(currentPosition)
        }

        listUserAdapter.setOnItemBoundCallback(object : UserAdapter.OnItemBindCallback {
            override fun onItemBound(username: String?, position: Int) {
                if (username != null && isBounded[position] == 0) {
                    viewModel.setDetailUser(username, position, token)
                }
                isBounded[currentPosition] = 1
            }
        })

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