package com.example.githubuser.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.helpers.ListHandler
import com.example.githubuser.viewmodels.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private var rvHandler = ListHandler()
    private lateinit var favViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.favorite_user)

        favViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FavoriteViewModel::class.java)

        favViewModel.getDbReadResult().observe(this) { favorites ->
            if (favorites.size > 0) {
                applicationContext?.let {
                    rvHandler.showFavoriteListView(binding.lvFavorite, favViewModel, favorites, it, this)
                }
            } else {
                showSnackBarMessage(getString(R.string.no_favorite))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        favViewModel.loadFavoritesAsync(this)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.lvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }
}