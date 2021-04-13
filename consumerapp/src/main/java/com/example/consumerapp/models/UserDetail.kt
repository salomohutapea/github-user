package com.example.consumerapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDetail(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("login")
    val username: String? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("avatar_url")
    val avatar: String? = null,

    @field:SerializedName("followers")
    val followers: String? = "0",

    @field:SerializedName("following")
    val following: String? = "0",

    @field:SerializedName("company")
    val company: String? = null,

    @field:SerializedName("public_repos")
    val public_repos: String? = "0",

    val _ID: String = "0"
) : Serializable