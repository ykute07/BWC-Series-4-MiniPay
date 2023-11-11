package com.example.pockemon_newer

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    var boolcheck = true
    var location1: Location? = null
    private var marker: Marker? = null
    private var bounceHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                val location = locationResult.lastLocation
                // Handle the new location here
                updateLocationOnMap(location)
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermission()
        loadPockemon()
    }
    companion object {
        private const val ACCESS_LOCATION = 123
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION)
                return
            }
        }

        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        getUserLocation()
    }

    private fun getUserLocation() {
        Toast.makeText(this, "User location access on", Toast.LENGTH_LONG).show()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ACCESS_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation()
                } else {
                    Toast.makeText(this, "We cannot access your location", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()

        // Set a minimum zoom level
        // Add an OnCameraIdleListener to handle camera position changes

    }

    private fun setMapStyle() {
        try {
            val success = mMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            )
            if (!success!!) {
                Log.e(TAG, "Custom map style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Resource not found: ${e.localizedMessage}")
        }
    }

    private fun updateLocationOnMap(location: Location) {
        mMap?.clear()
        val userLocation = LatLng(location.latitude, location.longitude)
        mMap?.addMarker(
            MarkerOptions()
                .position(userLocation)
                .title("Me")
                .snippet("Here is my location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.traveller1))
        )
        startBounceAnimation()

        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
    }

    var playerpower = 0.0
    var listpockemons = ArrayList<MapX>()

    fun loadPockemon() {
        var lat = intent.getStringExtra("LAT")?.replace(" ", "")
        var long = intent.getStringExtra("LONG")?.replace(" ", "")
        if (lat.isNullOrEmpty()) {
            lat = "19.878895"
        }
        if (long.isNullOrEmpty()) {
            long = "75.354420"
        }
        listpockemons.add(
            MapX(
                R.drawable.cart,
                "Taj Mahal",
                "One of a Kind Art",
                55.0,
                27.17381,
                78.0421
            )
        )
        listpockemons.add(
            MapX(
                R.drawable.cityilluminati,
                "Paris",
                "City Of Illumination",
                90.5,
                48.8566,
                2.3522
            )
        )
        listpockemons.add(
            MapX(
                R.drawable.collectiable1,
                "Irish Boho",
                "Mint and Get 10% off on Hats",
                33.5,
                28.5016,
                77.0952
            )
        )
        listpockemons.add(
            MapX(
                R.drawable.collectiables,
                "Tie Dye",
                "Mint and Get 50% off on Shirts",
                33.5,
                -22.9519,
                -43.2105
            )
        )
        listpockemons.add(
            MapX(
                R.drawable.mcdonald,
                "Ronald McDonald Spacecraft",
                "I'm lovin' Web3, Mint it and get 5% off",
                33.5,
                -33.8688,
                151.2093
            )
        )
        listpockemons.add(
            MapX(
                R.drawable.movietheatre,
                "Theatre La Gazelle",
                "Live The Movie",
                33.5,
                36.1716,
                -115.1391
            )
        )

        listpockemons.add(
            MapX(
                R.drawable.location,
                "Tokyo",
                "Endless Discovery",
                33.5,
                33.6762,
                139.6503
            )
        )

        listpockemons.add(
            MapX(
                R.drawable.voucher,
                "Chai Offer",
                "A healthy sip and 5% off for Everyone",
                33.5,
                lat.toDouble(),
                long.toDouble()
            )
        )
        listpockemons.add(
            MapX(
                R.drawable.spacelocation,
                "Space NFT",
                "Too The MOON ticket",
                33.5,
                28.573469,
                -80.651070
            )
        )
        listpockemons.add(
            MapX(
                R.drawable.disneyland,
                "Disney Land",
                "Happiest Place on Earth",
                33.5,
                55.3871,
                3.4360
            )
        )
    }

    private fun startBounceAnimation() {
        val initialY = marker?.position?.latitude ?: 0.0
        var y = initialY
        val bounceHeight = 0.0002 // Adjust this value for desired bounce height
        val bounceDuration = 1000L // Adjust the duration of each bounce cycle in milliseconds

        val bounceRunnable = object : Runnable {
            override fun run() {
                // Calculate the new marker position for bounce effect
                y = if (y == initialY) y + bounceHeight else initialY

                val newPosition = LatLng(y, marker?.position?.longitude ?: 0.0)
                marker?.position = newPosition

                // Repeat the animation after a delay
                bounceHandler.postDelayed(this, bounceDuration)
            }
        }

        // Start the initial bounce
        bounceHandler.post(bounceRunnable)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.collection, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.homePage -> {
                startActivity(
                    Intent(
                        this@MapsActivity,
                        CollectionActivity::class.java
                    )
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}







