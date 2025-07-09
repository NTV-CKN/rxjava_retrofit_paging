package com.example.learnretrofit.data.model

import com.google.gson.annotations.SerializedName
import lombok.Data

@Data
data class Repo(
    @field:SerializedName("private")
    val jsonMemberPrivate: Boolean? = null,

    @field:SerializedName("full_name")
    val fullName: String? = null,

    @field:SerializedName("permissions")
    val permissions: Permissions? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Long? = null,

    @field:SerializedName("node_id")
    val nodeId: String? = null
)