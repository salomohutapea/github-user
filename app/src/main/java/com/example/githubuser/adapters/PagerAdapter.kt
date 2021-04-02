package com.example.githubuser.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.views.FollowFragment
import com.example.githubuser.models.Users

class PagerAdapter(
    activity: AppCompatActivity,
    private var following: ArrayList<Users>,
    private var followers: ArrayList<Users>
) :
    FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position + 1, following, followers)
    }

    override fun getItemCount(): Int {
        return 2
    }
}