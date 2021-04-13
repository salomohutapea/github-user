package com.example.githubuser.views

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
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.handlers.ErrorHandler
import com.example.githubuser.handlers.ListHandler
import com.example.githubuser.viewmodels.MainViewModel
import com.example.githubuser.viewmodels.UserListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userListViewModel: UserListViewModel
    private var rvHandler = ListHandler()
    private var errHandler = ErrorHandler()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        // Add github token to string resource
        token = getString(R.string.github_token)

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
                showLoading(true)
                mainViewModel.setSearchUser(username = query, token = token)
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
        } else if (item.itemId == R.id.favorite) {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeViewModel() {

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        userListViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserListViewModel::class.java)

        userListViewModel.getStatus().observe(this) {
            applicationContext?.let { context -> errHandler.errorMessage(it, context) }
        }

        mainViewModel.getStatus().observe(this) {
            applicationContext?.let { context -> errHandler.errorMessage(it, context) }
        }

        mainViewModel.getSearchResult().observe(this) {
            if (it.size != 0) {
                rvHandler.list = it
                showRecyclerList()
            }
        }

        userListViewModel.getLoading().observe(this) {
            showLoading(it)
        }
        mainViewModel.setSearchUser(username = "salomohutapea", token = token)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.rvUsers.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvUsers.visibility = View.VISIBLE
        }
    }

    private fun showRecyclerList() {
        applicationContext?.let {
            rvHandler.showUserRecyclerView(binding.rvUsers, userListViewModel, this, it, token)
        }
    }
}