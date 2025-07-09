package com.example.learnretrofit.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.example.learnretrofit.RetrofitHelper

import com.example.learnretrofit.data.model.Repo
import com.example.learnretrofit.data.remote.RepoPagingSource
import io.reactivex.rxjava3.core.Flowable

class RepoViewModel(private val userName: String) : ViewModel() {
    private val pager = Pager(PagingConfig(20, initialLoadSize = 20)) {
        RepoPagingSource(
            RetrofitHelper.getInstanceOfGithubApi(),
            userName
        )
    }

    fun getFlowable(): Flowable<PagingData<Repo>> {
        val flowable = pager.flowable
      return  flowable.replay(1).refCount()
    }
}