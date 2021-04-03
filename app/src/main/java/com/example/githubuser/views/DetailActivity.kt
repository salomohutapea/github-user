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
import com.example.githubuser.handlers.ErrorHandler
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add github token to string resource
        token = getString(R.string.github_token)


        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)

        val username = intent.getSerializableExtra("USERNAME") as String

        detailViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        true.showLoading()
        detailViewModel.setDetailUser(username, token)
        detailViewModel.setFollowers(username, token)
        detailViewModel.setFollowing(username, token)

        detailViewModel.getStatus().observe(this) {
            applicationContext?.let { context -> ErrorHandler().errorMessage(it, context) }
        }

        detailViewModel.getSearchResult().observe(this) { data ->
            updateView(data)
            false.showLoading()
        }

        detailViewModel.getFollowing().observe(this) {
            following = it
            displayPager()
        }
        detailViewModel.getFollowers().observe(this) {
            followers = it
            displayPager()
        }

    }

    private fun displayPager() {
        pagerAdapter = PagerAdapter(this, following, followers)
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Following"
                1 -> tab.text = "Followers"
            }
        }.attach()
        supportActionBar?.elevation = 0f
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
            binding.progressBar.visibility = View.VISIBLE
            binding.constraintDetailAll.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.constraintDetailAll.visibility = View.VISIBLE
        }
    }
}