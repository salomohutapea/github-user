package com.example.githubuser

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.databinding.ActivityDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var mMap: GoogleMap
    private lateinit var userAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        val userData = intent.getParcelableExtra<User>("DATA_USER")
        if (userData != null) {
            userData.location?.let { userAddress = it }
            userData.avatar?.let { binding.detailImgUser.setImageResource(it) }
            userData.company?.let { binding.detailTvCompany.text = it }
            userData.name?.let { binding.detailTvName.text = it }
            userData.username?.let { binding.detailTvUsername.text = it }
            userData.followers?.let { binding.detailTvFollowers.text = it }
            userData.following?.let { binding.detailTvFollowing.text = it }
            userData.repository?.let { binding.detailTvRepository.text = it }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(1f)
        mMap.setMaxZoomPreference(30f)
        updateMap(userAddress)
    }

    private fun updateMap(userLocation: String) {
        val userLatLng = getLatLngfromLocation(userLocation)
        mMap.addMarker(
            MarkerOptions()
                .position(userLatLng)
                .title(userLocation)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12f))

    }

    private fun getLatLngfromLocation(strAddress: String): LatLng {
        val coder = Geocoder(this)
        val address: List<Address>?

        return try {
            address = coder.getFromLocationName(strAddress, 5)

            val location: Address = address[0]
            LatLng(location.latitude, location.longitude)

        } catch (e: IOException) {
            e.printStackTrace()
            LatLng(-6.175392, 106.827153)
        }
    }
}