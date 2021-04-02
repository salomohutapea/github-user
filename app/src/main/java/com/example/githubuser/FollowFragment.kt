package com.example.githubuser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.adapters.UserAdapter
import com.example.githubuser.models.Users
import com.example.githubuser.viewmodels.DetailViewModel
import com.example.githubuser.viewmodels.UserListViewModel

class FollowFragment : Fragment() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var userListViewModel: UserListViewModel
    private lateinit var rvFollow: RecyclerView
    private var list = ArrayList<Users>()
    private var currentPosition = 0
    private var isBounded = IntArray(100){0}

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val FOLLOWING = "following"
        private const val FOLLOWERS = "followers"

        @JvmStatic
        fun newInstance(index: Int, following: ArrayList<Users>, followers: ArrayList<Users>) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putSerializable(FOLLOWING, following)
                    putSerializable(FOLLOWERS, followers)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        userListViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserListViewModel::class.java)
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFollow = view.findViewById(R.id.rv_follow)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        rvFollow.itemAnimator = null
        currentPosition = 0
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        if (index == 1)
            list = arguments?.getSerializable(FOLLOWING) as ArrayList<Users>
        else if (index == 2)
            list = arguments?.getSerializable(FOLLOWERS) as ArrayList<Users>

        rvFollow.layoutManager = LinearLayoutManager(context)
        val listUserAdapter = UserAdapter(list)
        rvFollow.adapter = listUserAdapter

        userListViewModel.getDetailResult().second.observe(this) {
            currentPosition = it
        }

        userListViewModel.getDetailResult().first.observe(this) {
            list[currentPosition].followers = it.followers
            list[currentPosition].public_repos = it.public_repos
            list[currentPosition].following = it.following
            listUserAdapter.notifyItemChanged(currentPosition)
        }

        listUserAdapter.setOnItemBoundCallback(object : UserAdapter.OnItemBindCallback {
            override fun onItemBound(username: String?, position: Int) {
                if (username != null && isBounded[position] == 0) {
                    userListViewModel.setDetailUser(username, position)
                }
                isBounded[currentPosition] = 1
            }
        })

        listUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("USERNAME", data.username)
                startActivity(intent)
            }
        })
    }
}