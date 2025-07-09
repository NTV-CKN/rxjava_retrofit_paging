package com.example.learnretrofit.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.learnretrofit.data.model.Repo
import com.example.learnretrofit.databinding.ItemRepoBinding
import com.example.learnretrofit.ui.OnCreateViewHolder

private val diffUtilsItem = object : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo) = oldItem == newItem
}

class RepoPagingDataAdapter (private val onCreateViewHolder: OnCreateViewHolder):
    PagingDataAdapter<Repo, RepoPagingDataAdapter.ViewHolder>(diffCallback = diffUtilsItem) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position) ?: Repo())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRepoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        onCreateViewHolder.onCreate()
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(repo: Repo) {
            binding.tvIdRepo.text = "" + repo.id
            binding.tvNameRepo.text = repo.fullName
            binding.tvAdminRepo.text = "" + repo.permissions?.admin
            binding.tvMaintainRepo.text = "" + repo.permissions?.maintain
            binding.tvPrivateRepo.text = "" + repo.jsonMemberPrivate
        }
    }
}