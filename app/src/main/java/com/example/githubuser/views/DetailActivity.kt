package com.example.githubuser.views

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapters.PagerAdapter
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.models.UserDetail
import com.example.githubuser.models.Users
import com.example.githubuser.viewmodels.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var flag: Int = 0
    private var maxFlag: Int = 0
    private var following = ArrayList<Users>()
    private var followers = ArrayList<Users>()
    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)

        val username = intent.getSerializableExtra("USERNAME") as String

        detailViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        true.showLoading()
        detailViewModel.setDetailUser(username)
        detailViewModel.setFollowers(username)
        detailViewModel.setFollowing(username)

        detailViewModel.getSearchResult().observe(this) { data ->
            flag++

            maxFlag = if(data.followers == "0" && data.following == "0") {
                1
            } else if((data.followers == "0" && data.following != "0") || (data.followers != "0" && data.following == "0")) {
                2
            } else {
                3
            }
            Log.d("MAXFLAGABC", maxFlag.toString())
            updateView(data)
            false.showLoading()
        }

        detailViewModel.getFollowing().observe(this) { data ->
            flag++
            following = data
            displayPager()
        }
        detailViewModel.getFollowers().observe(this) { data ->
            flag++
            followers = data
            displayPager()
        }

    }

    private fun displayPager() {
        if (flag == maxFlag) {
            val pagerAdapter = PagerAdapter(this, following, followers)
            viewPager.adapter = pagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Following"
                    1 -> tab.text = "Followers"
                }
            }.attach()
            supportActionBar?.elevation = 0f
        } else return
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