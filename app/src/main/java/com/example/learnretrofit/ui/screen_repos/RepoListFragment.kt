package com.example.learnretrofit.ui.screen_repos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import autodispose2.AutoDispose.autoDisposable
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.example.learnretrofit.ViewModelFactoryHelper
import com.example.learnretrofit.databinding.FragmentRepoListBinding
import com.example.learnretrofit.ui.OnCreateViewHolder
import com.example.learnretrofit.ui.adapter.RepoPagingDataAdapter
import com.example.learnretrofit.ui.viewmodel.RepoViewModel
import com.example.learnretrofit.ui.viewmodel.UserViewModel

class RepoListFragment : Fragment() {
    private lateinit var adapter: RepoPagingDataAdapter
    private lateinit var binding: FragmentRepoListBinding
    private lateinit var userViewModel: UserViewModel
    private val repoViewModel: RepoViewModel by lazy {
        Log.d("SVU", "lazy ${userViewModel.user.value}")
        ViewModelProvider(
            viewModelStore,
            ViewModelFactoryHelper(userViewModel.user.value?.userName ?: "")
        )[RepoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapterAndRecyclerView()
        initViewModels()
    }

    private fun initAdapterAndRecyclerView() {
        adapter = RepoPagingDataAdapter(object : OnCreateViewHolder {
            override fun onCreate() {
                binding.progressBarRepoList.visibility = View.GONE
            }

        })
        binding.recyclerView.adapter = adapter
    }

    private fun initViewModels() {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("SVU", "Callback of user view model $user")
            repoViewModel.getFlowable()
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe {
                    Log.d("SVU", "return pagingdata")
                    adapter.submitData(lifecycle, it)
                }
        }
    }
}