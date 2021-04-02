package com.example.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapters.UserAdapter
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.models.UserDetail
import com.example.githubuser.models.Users
import com.example.githubuser.viewmodels.MainViewModel
import com.example.githubuser.viewmodels.UserListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userListViewModel: UserListViewModel
    private var list = ArrayList<Users>()
    private var detailList = ArrayList<UserDetail>()
    private var currentPosition = 0
    private var isBounded = IntArray(100){0}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViewModel()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) return true
                true.showLoading()
                isBounded = IntArray(100){0}
                mainViewModel.setSearchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeViewModel() {
        true.showLoading()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        userListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserListViewModel::class.java)
        mainViewModel.getSearchResult().observe(this) {
            if (it.size != 0) {
                list = it
                showRecyclerList()
            }
            false.showLoading()
        }
        mainViewModel.setSearchUser("salomohutapea")
    }

    private fun Boolean.showLoading() {
        if (this) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerList() {
        binding.rvUsers.itemAnimator = null
        currentPosition = 0
        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        val listUserAdapter = UserAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("USERNAME", data.username)
                startActivity(intent)
            }
        })

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
    }
}