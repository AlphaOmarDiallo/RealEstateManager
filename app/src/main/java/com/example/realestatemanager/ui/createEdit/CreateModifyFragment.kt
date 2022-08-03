package com.example.realestatemanager.ui.createEdit

import android.content.ContentValues.TAG
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.BuildConfig
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.nearBySearch.Result
import com.example.realestatemanager.data.viewmodel.CreateEditViewModel
import com.example.realestatemanager.databinding.FragmentCreateModifyBinding
import com.example.realestatemanager.domain.Constant
import com.example.realestatemanager.domain.Utils
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CreateModifyFragment : Fragment() {

    private lateinit var binding: FragmentCreateModifyBinding
    lateinit var viewModel: CreateEditViewModel

    private var isCloseToSchool = false
    private var isCloseToParc = false
    private var isCloseToShops = false
    private var isCloseToTransport = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateModifyBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_mortgage_calculator, container, false)
        viewModel = ViewModelProvider(requireActivity())[CreateEditViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPhoto.setOnClickListener {
            takePhoto.launch()
        }

        viewModel.listInterest.observe(requireActivity(), this::checkList)

        // Initialize the SDK
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(requireContext())

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG))
        autocompleteFragment.setCountries("US")
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)
        autocompleteFragment.setHint("Address")
        /*val bounds = RectangularBounds.newInstance(
            LatLng(40.524930, -73.668323),
            LatLng(41.025635, -74.253667)
        )
        autocompleteFragment.setLocationRestriction(bounds)*/

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                binding.TIETPropertyAddress.setText(place.address)

                val location = Location("Place")
                location.latitude = place.latLng.latitude
                location.longitude = place.latLng.longitude

                viewModel.getInterestsAround(location)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }

    private val takePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            val fileName = UUID.randomUUID().toString()
            val isSavedSuccessfully =
                viewModel.savePhotoToInternalStorage(fileName, it!!, requireContext())
            if (isSavedSuccessfully) Utils.snackBarMaker(
                binding.root,
                "Photo saved successfully"
            ) else Utils.snackBarMaker(binding.root, "Error saving photo")
            val test = viewModel.getPhotoPath(requireContext(), fileName)
            Utils.snackBarMaker(binding.root, test)
        }

    private fun checkList(list: List<Result>) {
        Log.e(TAG, "checkList: $list")
        for (item in list) {
            Log.i(TAG, "checkList: ${item.types}")
            if (item.types.toString().contains(Constant.SCHOOL)) {
                isCloseToSchool = true
                Log.i(TAG, "school")
            }
            if (item.types.toString().contains(Constant.SHOPS)) {
                isCloseToShops = true
                Log.i(TAG, "shops")
            }
            if (item.types.toString().contains(Constant.PARK)) {
                isCloseToParc = true
                Log.i(TAG, "park")
            }
            if (item.types.toString().contains(Constant.TRANSPORT)) {
                isCloseToTransport = true
                Log.i(TAG, "transport")
            }
        }
    }
}