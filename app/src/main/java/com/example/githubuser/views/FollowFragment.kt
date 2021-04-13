package com.example.githubuser.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.helpers.ErrorHandler
import com.example.githubuser.helpers.ListHandler
import com.example.githubuser.models.Users
import com.example.githubuser.viewmodels.UserListViewModel

class FollowFragment : Fragment() {

    private lateinit var userListViewModel: UserListViewModel
    private lateinit var rvFollow: RecyclerView
    private lateinit var binding: FragmentFollowBinding
    private var rvHandler = ListHandler()
    private lateinit var token: String

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
    ): View {
        token = getString(R.string.github_token)
        binding = FragmentFollowBinding.inflate(inflater, container, false)

        // Add github token to string resource
        token = getString(R.string.github_token)

        userListViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserListViewModel::class.java)
        userListViewModel.getStatus().observe(this) {
            context?.let { context -> ErrorHandler().errorMessage(it, context) }
        }
        userListViewModel.getLoading().observe(this) {
            showLoading(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFollow = view.findViewById(R.id.rv_follow)
        showRecyclerList()
    }

    private fun showRecyclerList() {
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        if (index == 1)
            rvHandler.list = arguments?.getSerializable(FOLLOWING) as ArrayList<Users>
        else if (index == 2)
            rvHandler.list = arguments?.getSerializable(FOLLOWERS) as ArrayList<Users>

        context?.let {
            rvHandler.showUserRecyclerView(rvFollow, userListViewModel, this, it, token)
        }
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.rvFollow.visibility = View.GONE
            binding.progressFragment.visibility = View.VISIBLE
        } else {
            binding.progressFragment.visibility = View.GONE
            binding.rvFollow.visibility = View.VISIBLE
        }
    }

}