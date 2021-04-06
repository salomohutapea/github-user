package com.example.githubuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.databinding.ItemRowUsersBinding
import com.example.githubuser.models.Users

class UserAdapter(private val listUser: ArrayList<Users>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_users, viewGroup, false)
        val holder = ListViewHolder(view)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
        return holder
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .into(holder.binding.singleImgUser)
        holder.binding.singleUsernameUser.text = user.username
        holder.binding.singleRepoUser.text = user.public_repos
        holder.binding.singleTvFollowers.text = user.followers
        holder.binding.singleTvFollowing.text = user.following
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var binding = ItemRowUsersBinding.bind(itemView)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Users)
    }
}