package com.example.realestatemanager.ui

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.R
import com.example.realestatemanager.data.viewmodel.MapsViewModel
import com.example.realestatemanager.databinding.FragmentGoogleMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoogleMapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var viewModel: MapsViewModel
    private lateinit var binding: FragmentGoogleMapsBinding
    private lateinit var location: Location

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap

        addOfficeMarker()

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.741695, -73.989569), 19f))
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.uiSettings.isMapToolbarEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoogleMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        setupViewModel()
        location = viewModel.getOfficeLocation()!!
        trackLocation()
        observeLocation()
        mapFragment?.getMapAsync(callback)
    }

    private fun addOfficeMarker() {
        val officeNY = viewModel.getOfficeLocation()
        val latLngOfficeNY = LatLng(40.741695, -73.989569)
        map.addMarker(
            MarkerOptions()
                .position(latLngOfficeNY)
                .title("Agency")
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(latLngOfficeNY))
    }

    /**
     * ViewModel setup
     */
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MapsViewModel::class.java]
    }

    /**
     * Location setup
     */

    private fun trackLocation() {
        viewModel.startLocationTracking(requireActivity(), requireContext())
    }

    private fun observeLocation() {
        viewModel.getCurrentLocation()?.observe(requireActivity()) { updateLocation(it) }
    }

    private fun updateLocation(newLocation: Location?) {
        if (this.isAdded) {
            if (newLocation != null) {
                location = newLocation
                enableMyLocation(map)
                getCurrentLocation(map)
            }
        }
    }

    private fun enableMyLocation(map: GoogleMap) {
        map.isMyLocationEnabled = true
    }

    private fun getCurrentLocation(googleMap: GoogleMap) {
        if (location != null) {
            val current = LatLng(location.latitude, location.longitude)
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    current,
                    19f
                )
            )
        }
    }
}