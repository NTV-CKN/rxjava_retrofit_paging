package com.example.learnretrofit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnretrofit.data.DataApi
import com.example.learnretrofit.ui.viewmodel.RepoViewModel

class ViewModelFactoryHelper(private val userName: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RepoViewModel::class.java))
            return RepoViewModel(userName) as T
        throw IllegalArgumentException("Don't support this model class!")
    }
}