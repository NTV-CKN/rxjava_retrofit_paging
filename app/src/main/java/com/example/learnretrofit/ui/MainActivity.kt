package com.example.learnretrofit.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import autodispose2.AutoDispose.autoDisposable
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.bumptech.glide.Glide
import com.example.learnretrofit.R
import com.example.learnretrofit.RetrofitHelper
import com.example.learnretrofit.UtilsHelper
import com.example.learnretrofit.data.DataApi

import com.example.learnretrofit.data.model.User
import com.example.learnretrofit.databinding.ActivityMainBinding
import com.example.learnretrofit.ui.screen_repos.RepoListFragment
import com.example.learnretrofit.ui.viewmodel.UserViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.UnknownHostException


class MainActivity : AppCompatActivity(), OnUserClick {
    private lateinit var binding: ActivityMainBinding
    private lateinit var githubApi: DataApi
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        githubApi = RetrofitHelper.getInstanceOfGithubApi()
        initEvents()
    }

    //TODO: Set events
    private fun initEvents() {
        binding.edtFind.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.includeInfoUser.main.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.edtFind.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.edtFind.text.toString().isNotEmpty()) {
                    binding.progressBar.visibility = View.VISIBLE
                    handleSingleUser(
                        githubApi.getUserByUserName(
                            binding.edtFind.text.toString().trim()
                        )
                    )
                }
                true
            } else
                false
        }
    }

    private fun handleSingleUser(singleUser: Single<User>?) {
        if (singleUser === null) {
            UtilsHelper.displaySnackBar(this, binding.root, "Not found!")
            return
        }

        singleUser.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe { user, throwable ->
                binding.progressBar.visibility = View.GONE
                throwable?.let {
                    if (throwable is UnknownHostException) {
                        UtilsHelper.displaySnackBar(
                            this,
                            binding.root,
                            "Please connect wifi or 4g/5g!"
                        )
                        return@subscribe
                    } else {
                        binding.includeInfoUser.main.visibility = View.GONE
                        UtilsHelper.displaySnackBar(this, binding.root, "Not found!")
                        return@subscribe
                    }
                }
                user?.let {
                    onBindUser(it)
                    binding.includeInfoUser.main.visibility = View.VISIBLE
                } ?: UtilsHelper.displaySnackBar(
                    this,
                    binding.root,
                    "Not found!"
                )
            }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun onBindUser(user: User) {
        binding.includeInfoUser.includeTvId.text = "" + user.id
        binding.includeInfoUser.includeTvName.text = user.name
        Glide.with(binding.root)
            .load(user.avatarUrl)
            .error(getDrawable(R.drawable.ic_null))
            .into(binding.includeInfoUser.imgInclude)

        binding.includeInfoUser.main.setOnClickListener { this.onUserClick(user) }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        var fragmentT: Fragment? = null

        if (fragment is RepoListFragment) {
            fragmentT = fragment
        }
        fragmentT?.let {
            binding.container.visibility = View.VISIBLE
            binding.containerWidget.visibility = View.GONE

            transaction.replace(binding.container.id, it)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        } ?: throw IllegalArgumentException("Don't support this fragment")
    }

    override fun onBackPressed() {
        if (!binding.containerWidget.isVisible) {
            supportFragmentManager.popBackStack()
            binding.containerWidget.visibility = View.VISIBLE
            binding.container.visibility = View.GONE
        } else
            super.onBackPressed()
    }

    override fun onUserClick(user: User) {
        UtilsHelper.closeSoftKeyboard(this, binding.root)
        if (UtilsHelper.checkNextWorkAvailable(this)) {
            userViewModel.postUser(user)
            openFragment(RepoListFragment())
        } else
            UtilsHelper.displaySnackBar(this, binding.root, "You must connect Internet!")
    }
}

interface OnUserClick {
    fun onUserClick(user: User)
}

interface OnCreateViewHolder {
    fun onCreate()
}