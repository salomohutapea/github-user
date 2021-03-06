package com.example.githubuser.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapters.PagerAdapter
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.helpers.ErrorHandler
import com.example.githubuser.models.UserDetail
import com.example.githubuser.models.Users
import com.example.githubuser.viewmodels.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout
    private lateinit var token: String
    private lateinit var pagerAdapter: PagerAdapter
    private var following = ArrayList<Users>()
    private var followers = ArrayList<Users>()
    private var flag = 0
    private var data = UserDetail()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_user)

        true.showLoading()

        // Add github token to string resource
        token = getString(R.string.github_token)
        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)

        initializeViewModel()

    }

    private fun initializeViewModel() {

        val username = intent.getSerializableExtra("USERNAME") as String

        detailViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        detailViewModel.getStatus().observe(this) {
            if (it == 444) {
                applicationContext?.let { context -> ErrorHandler().errorMessage(it, context) }
            }
        }

        detailViewModel.getIsUserFavorite().observe(this) {
            it.isUserFavorite()
        }

        detailViewModel.getDetailResult().observe(this) { data ->
            updateView(data)
            this.data = data
            flag++
        }

        detailViewModel.getFollowing().observe(this) {
            following = it
            if (it.size != 0)
                displayPager()
        }
        detailViewModel.getFollowers().observe(this) {
            followers = it
            if (it.size != 0)
                displayPager()
        }

        detailViewModel.checkIfUserExists(username, this)
        detailViewModel.setDetailUser(username, token)
        detailViewModel.setFollowers(username, token)
        detailViewModel.setFollowing(username, token)

        binding.fabFavorite?.setOnClickListener {
            detailViewModel.addOrDeleteFavorite(data, applicationContext)
            binding.fabFavorite?.setImageResource(R.drawable.ic_baseline_delete_24)
        }
    }

    private fun displayPager() {
        flag++
        supportActionBar?.elevation = 0f
        pagerAdapter = PagerAdapter(this, following, followers)
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.following)
                1 -> tab.text = getString(R.string.followers)
            }
        }.attach()

        if (flag == 3 && followers.size != 0 && following.size != 0)
            false.showLoading()
        else if (flag == 2 && (followers.size == 0 || following.size == 0))
            false.showLoading()
        else if (flag == 1 && followers.size == 0 && following.size == 0)
            false.showLoading()
    }

    private fun updateView(data: UserDetail) {
        Glide.with(applicationContext)
            .load(data.avatar)
            .into(binding.detailImgUser)
        binding.detailTvCompany.text = data.company
        binding.detailTvFollowers.text = data.followers
        binding.detailTvName.text = data.name
        binding.detailTvFollowing.text = data.following
        binding.detailTvRepository.text = data.public_repos
        binding.detailTvUsername.text = data.username
        binding.detailTvLocation.text = data.location
    }

    private fun Boolean.showLoading() {
        if (this) {
            binding.progressDetail?.visibility = View.VISIBLE
            binding.fabFavorite?.visibility = View.GONE
            binding.constraintDetailAll.visibility = View.GONE
        } else {
            binding.progressDetail?.visibility = View.GONE
            binding.constraintDetailAll.visibility = View.VISIBLE
            binding.fabFavorite?.visibility = View.VISIBLE
        }
    }

    private fun Boolean.isUserFavorite() {
        if (this) {
            binding.fabFavorite?.setImageResource(R.drawable.ic_baseline_delete_24)
        } else {
            binding.fabFavorite?.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
    }
}