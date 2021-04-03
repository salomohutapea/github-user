package com.example.githubuser.views

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.handlers.RecyclerViewHandler
import com.example.githubuser.viewmodels.MainViewModel
import com.example.githubuser.viewmodels.UserListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userListViewModel: UserListViewModel
    private var rvHandler = RecyclerViewHandler()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                true.showLoading()
                rvHandler.isBounded = IntArray(100){0}
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeViewModel() {
        true.showLoading()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        userListViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserListViewModel::class.java)

        userListViewModel.getStatus().observe(this) {
            it.errorMessage()
        }
        mainViewModel.getStatus().observe(this) {
            it.errorMessage()
        }

        mainViewModel.getSearchResult().observe(this) {
            if (it.size != 0) {
                rvHandler.list = it
                showRecyclerList()
            }
            false.showLoading()
        }

        mainViewModel.setSearchUser(username = "salomohutapea", token = token)
    }

    private fun Int.errorMessage() {
        if (this in 400..598) {
            Toast.makeText(applicationContext, "$this Cannot fetch data", Toast.LENGTH_SHORT).show()
            false.showLoading()
        }
        else if (this == 444) {
            Toast.makeText(applicationContext, "No connection", Toast.LENGTH_SHORT).show()
            false.showLoading()
        }
    }

    private fun Boolean.showLoading() {
        if (this) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerList() {
        applicationContext?.let {
            rvHandler.showRecyclerView(binding.rvUsers, userListViewModel, this, it, token)
        }
    }
}