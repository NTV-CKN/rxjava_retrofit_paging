package com.example.learnretrofit.data.model

import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class User(
    @field:SerializedName("login")
    val userName: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("id")
    val id: Long? = null,

    @field:SerializedName("name")
    val name: String? = null
)