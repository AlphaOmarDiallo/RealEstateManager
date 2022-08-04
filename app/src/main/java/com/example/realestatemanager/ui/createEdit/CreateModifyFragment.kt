package com.example.realestatemanager.ui.createEdit

import android.content.ContentValues.TAG
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.realestatemanager.BuildConfig
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.model.Property
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
    private lateinit var navController: NavController
    private val args: CreateModifyFragmentArgs by navArgs()

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
        getArgsFromNav()

        observeListAgent()

        observeNearBySearchResults()

        bindButtonPhoto()

        initPlace()

        setAutoComplete()

        updateViews()

    }

    /**
     * Photo intent
     */

    private fun bindButtonPhoto() {
        binding.btnAddPhoto.setOnClickListener {
            takePhoto.launch()
        }
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

    /**
     * Get args from NavHost
     */

    private fun getArgsFromNav() {
        navController = findNavController()
    }

    /**
     * Set autocomplete text views
     */

    private fun setAutocompleteTextViews(list: List<Agent>?) {
        setTypes()
        setAgent(list)
        Log.e(TAG, "setAutocompleteTextViews: here")
    }

    private fun setTypes() {
        if (args.property == null) {
            val listType =
                listOf("Apartment", "Penthouse", "Loft", "Duplex", "House", "Villa", "Mansion", "Other")
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listType)
            (binding.TILPropertyType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        } else {
            setupType(args.property!!.type)
        }

    }

    private fun setAgent(list: List<Agent>?) {
        val listAgentName: MutableList<String> = mutableListOf()
        if (list != null) {
            for (agent in list) {
                listAgentName.add(agent.name)
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listAgentName)
            (binding.TILAgentManagingProperty.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }
    }

    /**
     * Observe nearBySearchResults
     */

    private fun observeNearBySearchResults() {
        viewModel.listInterest.observe(requireActivity(), this::checkList)
    }

    private fun observeListAgent() {
        viewModel.getAgentList()
        viewModel.listAgent.observe(requireActivity(), this::setAutocompleteTextViews)
    }

    /**
     * Set close too
     */
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

    private fun saveProperty() {

    }

    /**
     * Google places
     */

    private fun initPlace() {
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
    }

    private fun setAutoComplete() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG))
        autocompleteFragment.setCountries(Constant.AUTOCOMPLETE_COUNTRY)
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)
        autocompleteFragment.setHint(getString(R.string.autocomplete_hint))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                binding.TIETPropertyAddress.setText(place.address)

                val location = Location("Place")
                location.latitude = place.latLng?.latitude!!
                location.longitude = place.latLng?.longitude!!

                viewModel.getInterestsAround(location)

            }

            override fun onError(status: Status) {
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }

    /**
     * Update Views
     */
    private fun updateViews() {
        if (args.property != null) {
            setupViewsWithPropertyInfo(args.property!!)
        }
    }

    private fun setupViewsWithPropertyInfo(property: Property) {
        setupAddress(property.address)
        setupNeighbourHood(property.neighbourhood)
        setupPrice(property.price)
        setupSurface(property.surface)
        setupRooms(property.numberOfRooms)
        setupBathrooms(property.numberOfBathrooms)
        setupBedRooms(property.numberOfBedrooms)
        setupDescription(property.description)
        setupSaleStatus(property.saleStatus)
        setupOnSaleSince(property.onTheMarketSince)
        setupSoldSince(property.offTheMarketSince)
        setupAgent(property.agentManagingPropertyId)
    }

    private fun setupAddress(address: String) {
        binding.TIETPropertyAddress.setText(address)
    }

    private fun setupNeighbourHood(neighbourhood: String) {
        binding.TIETPropertyNeighbourhood.setText(neighbourhood)
    }

    private fun setupType(type: String) {
        binding.autoCompleteTextViewType.setText(type.toString(), false)
    }

    private fun setupPrice(price: Int) {
        binding.TIETPropertyPrice.setText(price.toString())
    }

    private fun setupSurface(surface: Int) {
        binding.TIETPropertySurface.setText(surface.toString())
    }

    private fun setupRooms(rooms: Int) {
        binding.TIETPropertyNbRooms.setText(rooms.toString())
    }

    private fun setupBedRooms(rooms: Int) {
        binding.TIETPropertyNbBedrooms.setText(rooms.toString())
    }

    private fun setupBathrooms(rooms: Int) {
        binding.TIETPropertyNbBathrooms.setText(rooms.toString())
    }

    private fun setupDescription(description: String) {
        binding.TIETPropertyDescription.setText(description)
    }

    private fun setupSaleStatus(status: Boolean) {
        binding.toggleSaleStatus.isChecked = status
    }

    private fun setupOnSaleSince(date: Long) {
        binding.TIETPropertyOnMarketSince.setText(date.toString())
    }

    private fun setupSoldSince(date: Long?) {
        if (date != null) binding.TIETPropertyOnMarketSince.setText(date.toString())
    }

    private fun setupAgent(agentID: String?) {
        if (agentID != null) viewModel.getAgent(agentID)
    }

}