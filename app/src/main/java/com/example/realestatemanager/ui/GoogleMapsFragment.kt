package com.example.realestatemanager.ui

import android.content.ContentValues.TAG
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
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
    private lateinit var navController: NavController
    private val defaultZoom = 16f
    private val latLngOfficeNY = LatLng(40.741695, -73.989569)

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap

        addOfficeMarker()

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLngOfficeNY,
                defaultZoom
            )
        )

        //map.uiSettings.isMapToolbarEnabled = true
        //map.uiSettings.isMyLocationButtonEnabled = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        observePropertyList()
        mapFragment?.getMapAsync(callback)
        getArgsFromNav()
    }

    private fun addOfficeMarker() {
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
     * Get args from NavHost
     */

    private fun getArgsFromNav() {
        navController = findNavController()
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
        if (newLocation != null && this::map.isInitialized) {
            location = newLocation
            enableMyLocation(map)
        }
    }

    private fun enableMyLocation(googleMap: GoogleMap) {
        googleMap.isMyLocationEnabled = true
    }

    /**
     * Properties setup
     */

    private fun observePropertyList() {
        viewModel.getProperties()
        viewModel.propertyList.observe(requireActivity()) { putMarkersOnMap(it) }
    }

    private fun putMarkersOnMap(list: List<Property>) {

        for (property in list) {
            map.addMarker(
                MarkerOptions()
                    .title(
                        "${property.type}, ${property.surface} msq, " +
                                "${property.numberOfBedrooms} bed, ${property.numberOfBathrooms} bath," +
                                " $${property.price}"
                    )
                    .position(property.latLng)
            )
        }

        map.setOnInfoWindowClickListener {
            for (property in list) {
                if (property.latLng == it.position) {
                    Log.e(TAG, "putMarkersOnMap: here", null)
                    val action =
                        GoogleMapsFragmentDirections.actionGoogleMapsFragmentToPropertyDetail(
                            property.id
                        )
                    navController.navigate(action)
                }
            }
        }

    }

}