package com.example.learnretrofit.data

import com.example.learnretrofit.data.model.Repo
import com.example.learnretrofit.data.model.User
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DataApi {
    @GET("users/{username}")
    fun getUserByUserName(@Path("username") userName: String): Single<User>?

    @GET("users/{username}/repos")
    fun getRepoByUserName(
        @Path("username") userName: String,
        @Query("per_page")
        perPage: Int,
        @Query("page")
        page: Int
    ): Single<List<Repo>>
}