package com.example.learnretrofit.data.remote

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.learnretrofit.data.DataApi
import com.example.learnretrofit.data.model.Repo
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class RepoPagingSource(private val dataApi: DataApi, private val userName: String) :
    RxPagingSource<Int, Repo>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Repo>> {
        val keyCur = params.key ?: 0
        return dataApi.getRepoByUserName(userName, params.loadSize, keyCur)
            .subscribeOn(Schedulers.io())
            .map { listRepo -> handleListRepo(listRepo, keyCur, params.loadSize) }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        val pageClosest = state.closestPageToPosition(state.anchorPosition ?: 0)
        return if (pageClosest?.nextKey != null) pageClosest.nextKey!!.minus(1)
        else if (pageClosest?.prevKey != null) pageClosest.prevKey!!.plus(1)
        else null
    }

    private fun handleListRepo(
        listRepo: List<Repo>,
        keyCur: Int,
        loadSize: Int
    ): LoadResult<Int, Repo> {
        var keyNext: Int? = keyCur
        keyNext = if (listRepo.size < loadSize) {
            null
        } else {
            keyNext!! + 1
        }
        Log.d("SVU", "loadsize $loadSize nextKey $keyNext sizeList ${listRepo.size}")
        return LoadResult.Page(listRepo, null, keyNext)
    }

}