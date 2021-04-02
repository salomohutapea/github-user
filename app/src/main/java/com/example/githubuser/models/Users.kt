package com.example.githubuser.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchResponse(
    @field:SerializedName("items")
    val items: ArrayList<Users>? = null
)

data class Users(
    @field:SerializedName("login")
    val username: String? = null,

    @field:SerializedName("avatar_url")
    val avatar: String? = null,

    @field:SerializedName("public_repos")
    var public_repos: String? = null,

    @field:SerializedName("followers")
    var followers: String? = null,

    @field:SerializedName("following")
    var following: String? = null,
) : Serializable