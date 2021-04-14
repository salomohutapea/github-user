package com.example.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.R
import com.example.consumerapp.databinding.ItemRowFavoriteBinding
import com.example.consumerapp.models.UserDetail

class FavoritesAdapter(private val listUser: ArrayList<UserDetail>) : RecyclerView.Adapter<FavoritesAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_favorite, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        val binding = holder.binding

        binding.favUsername.text = user.username
        binding.favRepoUser.text = user.public_repos
        binding.favFollowers.text = user.followers
        binding.favFollowing.text = user.following

        Glide.with(holder.binding.favImage.context)
            .load(user.avatar)
            .into(holder.binding.favImage)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var binding = ItemRowFavoriteBinding.bind(itemView)
    }

}