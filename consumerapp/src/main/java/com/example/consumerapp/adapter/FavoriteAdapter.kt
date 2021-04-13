package com.example.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.daimajia.swipe.adapters.BaseSwipeAdapter
import com.example.consumerapp.R
import com.example.consumerapp.models.UserDetail


class FavoriteAdapter(private val listUser: ArrayList<UserDetail>) : BaseSwipeAdapter() {
    private lateinit var onDeleteClickCallback: OnDeleteClickCallback

    fun setOnDeleteCallback(onDeleteClickCallback: OnDeleteClickCallback) {
        this.onDeleteClickCallback = onDeleteClickCallback
    }

    override fun getCount(): Int {
        return listUser.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipelayout
    }

    override fun generateView(position: Int, parent: ViewGroup?): View {
        return LayoutInflater.from(parent?.context).inflate(R.layout.item_row_favorite, parent, false)
    }

    override fun fillValues(position: Int, convertView: View?) {
        val user = listUser[position]
        val textUsername = convertView?.findViewById(R.id.fav_username) as TextView
        val imgAvatar = convertView.findViewById(R.id.fav_image) as ImageView
        val deleteButton = convertView.findViewById(R.id.swipe_delete) as LinearLayout
        val txtRepo = convertView.findViewById(R.id.fav_repo_user) as TextView
        val txtFollowing = convertView.findViewById(R.id.fav_following) as TextView
        val txtFollowers = convertView.findViewById(R.id.fav_followers) as TextView

        deleteButton.setOnClickListener {
            onDeleteClickCallback.onItemClicked(user._ID, position)
        }

        textUsername.text = user.username
        txtRepo.text = user.public_repos
        txtFollowers.text = user.followers
        txtFollowing.text = user.following

        Glide.with(imgAvatar.context)
            .load(user.avatar)
            .into(imgAvatar)
    }

    interface OnDeleteClickCallback {
        fun onItemClicked(id: String, position: Int)
    }
}