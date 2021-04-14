package com.example.consumerapp.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.consumerapp.R
import com.example.consumerapp.databinding.ActivityMainBinding
import com.example.consumerapp.handlers.ListHandler
import com.example.consumerapp.viewmodels.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var rvHandler = ListHandler()
    private lateinit var favViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.consumer_app)

        favViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FavoriteViewModel::class.java)

        favViewModel.loadFav(this)
        favViewModel.getDbReadResult().observe(this) { favorites ->
            if (favorites.size > 0) {
                applicationContext?.let {
                    rvHandler.showFavoriteListView(binding.rvFavorite, favorites, this)
                }
            } else {
                showSnackBarMessage(getString(R.string.no_favorite))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        favViewModel.loadFav(this)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }
}