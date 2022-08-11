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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realestatemanager.BuildConfig
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.model.PlaceDetail
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.model.nearBySearch.Result
import com.example.realestatemanager.data.viewmodel.CreateEditViewModel
import com.example.realestatemanager.databinding.FragmentCreateModifyBinding
import com.example.realestatemanager.domain.Constant
import com.example.realestatemanager.domain.notification.PropertyNotificationService
import com.example.realestatemanager.domain.utils.Utils
import com.example.realestatemanager.ui.createEdit.adapter.ExternalStorageAdapter
import com.example.realestatemanager.ui.createEdit.adapter.PhotoListAdapter
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateModifyFragment : Fragment(),
    ExternalStorageAdapter.OnItemExternalStoragePhotoClickListener,
    PhotoListAdapter.OnItemPhotoListClickListener {

    private lateinit var binding: FragmentCreateModifyBinding
    private lateinit var navController: NavController
    private lateinit var placesClient: PlacesClient
    private lateinit var adapterExt: ExternalStorageAdapter
    private lateinit var photoListAdapter: PhotoListAdapter
    lateinit var viewModel: CreateEditViewModel
    private val args: CreateModifyFragmentArgs by navArgs()
    private val listInterestID: MutableList<String> = mutableListOf()
    private val listPlaceDetail: MutableList<PlaceDetail> = mutableListOf()
    private val listPhoto: MutableList<String> = mutableListOf()
    private var isCloseToSchool = false
    private var isCloseToPark = false
    private var isCloseToShops = false
    private var isCloseToTransport = false
    private var latLng: LatLng = LatLng(40.741694549848404, -73.98956985396345)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateModifyBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[CreateEditViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgsFromNav()

        setExternalPhotoAdapter()

        setPhotoListAdapter()

        observeNearBySearchResults()

        observeListAgent()

        setAutoComplete()

        initPlace()

        updateViews()

        bindButtonPhoto()

        bindButtonSave()
    }

    /**
     * Get args from NavHost
     */

    private fun getArgsFromNav() {
        navController = findNavController()
    }

    /**
     * List External Photo
     */
    private fun setExternalPhotoAdapter() {
        viewModel.initContentProvider(requireContext())

        adapterExt = ExternalStorageAdapter(ExternalStorageAdapter.ListDiff(), this)
        binding.recyclerview2.adapter = adapterExt
        binding.recyclerview2.layoutManager =
            (LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false))

        updateExternalRV()
    }

    private fun updateExternalRV() {
        viewModel.loadPhotoFromExternalStorage(requireContext())
        viewModel.listExternalPhoto.observe(requireActivity()) {
            it?.let {
                adapterExt.submitList(it)
            }
        }
    }

    override fun onItemExternalClick(position: Int) {
        val externalPhotoList = viewModel.listExternalPhoto.value
        val photo = externalPhotoList?.get(position)

        if (photo != null) {
            listPhoto.add(photo.contentUri.toString())
        }

        updatePhotoListRV()
    }

    /**
     * Display photos in property
     */
    private fun setPhotoListAdapter() {
        photoListAdapter = PhotoListAdapter(PhotoListAdapter.ListDiff(), this)
        binding.recyclerviewListPhoto.adapter = photoListAdapter
        binding.recyclerviewListPhoto.layoutManager =
            (LinearLayoutManager(view?.context, LinearLayoutManager.HORIZONTAL, false))

        updatePhotoListRV()
    }

    private fun updatePhotoListRV() {
        photoListAdapter.submitList(listPhoto)
    }

    override fun onItemPhotoListClick(position: Int) {
        listPhoto.removeAt(position)
        updatePhotoListRV()
    }

    /**
     * Observe data relative to nearBySearch and Agent in ViewModel
     */

    private fun observeNearBySearchResults() {
        viewModel.listInterest.observe(requireActivity(), this::checkList)
        viewModel.listInterest.observe(requireActivity(), this::createListPlacesID)
        viewModel.listInterest.observe(requireActivity(), this::createListPlaceDetail)
    }

    private fun observeListAgent() {
        viewModel.getAgentList()
        viewModel.listAgent.observe(requireActivity(), this::setAutocompleteTextViews)
    }

    /**
     * Set autocomplete text views
     */

    private fun setAutocompleteTextViews(list: List<Agent>?) {
        setTypesAutocomplete()
        setAgentAutoComplete(list)
    }

    private fun setTypesAutocomplete() {
        if (args.property == null) {
            val listType =
                listOf(
                    "Apartment",
                    "Penthouse",
                    "Loft",
                    "Duplex",
                    "House",
                    "Villa",
                    "Mansion",
                    "Studio",
                    "Other"
                )
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listType)
            (binding.TILPropertyType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        } else {
            setupType(args.property!!.type)
        }

    }

    private fun setAgentAutoComplete(list: List<Agent>?) {
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
     * Google places
     */

    private fun initPlace() {
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(requireContext())
    }

    private fun setAutoComplete() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS
            )
        )
        autocompleteFragment.setCountries(Constant.AUTOCOMPLETE_COUNTRY)
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)
        autocompleteFragment.setHint(getString(R.string.autocomplete_hint))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                val listAddressComponents = place.addressComponents?.asList()

                binding.TIETPropertyAddress.setText(place.address)

                if (listAddressComponents != null) {
                    for (item in listAddressComponents) {
                        if (item.types.toString()
                                .contains("sublocality_level_1")
                        ) binding.TIETPropertyNeighbourhood.setText(item.name) else binding.TIETPropertyNeighbourhood.setText(
                            ""
                        )
                    }

                    for (item in listAddressComponents) {
                        if (item.types.toString()
                                .contains("locality")
                        ) binding.TIETPropertyCity.setText(item.name)
                    }
                }

                val location = Location("Place")
                location.latitude = place.latLng?.latitude!!
                location.longitude = place.latLng?.longitude!!

                latLng = place.latLng!!

                viewModel.getInterestsAround(location)

            }

            override fun onError(status: Status) {
                Log.w(TAG, "An error occurred: $status")
            }
        })
    }

    /**
     * Set interests around
     */

    private fun createListPlacesID(list: List<Result>) {
        for (item in list) {
            listInterestID.add(item.place_id)
        }
    }

    private fun checkList(list: List<Result>) {
        for (item in list) {
            if (item.types.toString().contains(Constant.SCHOOL) || item.types.toString()
                    .contains(Constant.S_SCHOOL) || item.types.toString()
                    .contains(Constant.P_SCHOOL)
            ) {
                isCloseToSchool = true
            }
            if (item.types.toString().contains(Constant.SHOPS)) {
                isCloseToShops = true
            }
            if (item.types.toString().contains(Constant.PARK)) {
                isCloseToPark = true
            }
            if (item.types.toString().contains(Constant.TRANSPORT)) {
                isCloseToTransport = true
            }
        }
    }

    private fun createListPlaceDetail(list: List<Result>) {
        if (this::placesClient.isInitialized) {
            for (result in list) {

                val placeId = result.place_id

                val placeFields = listOf(Place.Field.ID, Place.Field.WEBSITE_URI)

                val request = FetchPlaceRequest.newInstance(placeId, placeFields)

                var placeResponse: Place? = null

                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response: FetchPlaceResponse ->
                        placeResponse = response.place
                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            Log.e(TAG, "Place not found: ${exception.message}")
                        }
                    }

                val place =
                    PlaceDetail(
                        placeID = result.place_id,
                        placeName = result.name,
                        placeLat = result.geometry.location.lat.toString(),
                        placeLng = result.geometry.location.lng.toString(),
                        placeWebSiteUri = placeResponse?.websiteUri.toString(),
                        placeType = result.types.toString()
                    )

                listPlaceDetail.add(place)
            }
        }

    }

    /**
     * Update Views
     */
    private fun updateViews() {
        if (args.property != null) {
            setupViewsWithPropertyInfo(args.property!!)
        } else {
            setupOnMarketSinceDate(null)
        }
    }

    private fun setupViewsWithPropertyInfo(property: Property) {
        setupAddress(property.address)
        setupNeighbourHood(property.neighbourhood)
        setupCity(property.city)
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
        setupListInterest(property.listOfInterest)
        setupListPhoto(property.photoIDList)
        setupOnMarketSinceDate(property.onTheMarketSince)
        setupLatLng(property.latLng)
    }

    private fun setupAddress(address: String) {
        binding.TIETPropertyAddress.setText(address)
    }

    private fun setupNeighbourHood(neighbourhood: String) {
        binding.TIETPropertyNeighbourhood.setText(neighbourhood)
    }

    private fun setupCity(city: String) {
        binding.TIETPropertyCity.setText(city)
    }

    private fun setupType(type: String) {
        binding.autoCompleteTextViewType.setText(type, false)
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
        val dateS = Utils.longToDate(date)
        binding.TIETPropertyOnMarketSince.setText(dateS)
    }

    private fun setupSoldSince(date: Long?) {
        if (date != null) binding.TIETPropertyOnMarketSince.setText(date.toString())
    }

    private fun setupAgent(agentID: String?) {
        if (agentID != null) viewModel.getAgent(agentID)
    }

    private fun setupListInterest(list: List<String>?) {
        if (!list.isNullOrEmpty()) listInterestID.addAll(list)
    }

    private fun setupListPhoto(list: List<String>) {
        listPhoto.addAll(list)
    }

    private fun setupOnMarketSinceDate(date: Long?) {
        if (date != null) {
            binding.TIETPropertyOnMarketSince.setText(Utils.longToDate(date = date))
        } else {
            binding.TIETPropertyOnMarketSince.setText(Utils.todayDate)
        }

    }

    private fun setupLatLng(latLng: LatLng) {
        this.latLng = latLng
    }

    /**
     * Photo management
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
                viewModel.savePhotoInExternalStorage(fileName, it!!, requireContext())
            //viewModel.savePhotoToInternalStorage(fileName, it!!, requireContext())
            if (isSavedSuccessfully) {
                Utils.snackBarMaker(binding.root, "Photo saved successfully")
                viewModel.loadPhotoFromExternalStorage(requireContext())
                adapterExt.submitList(viewModel.listExternalPhoto.value)
            } else {
                Utils.snackBarMaker(binding.root, "Error saving photo")
            }
        }

    /**
     * Save or update property
     */

    private fun createProperty(): Property {
        val id: Int = if (args.property == null) 0 else args.property!!.id
        val type: String = binding.autoCompleteTextViewType.text.toString()
        val price: Int = binding.TIETPropertyPrice.text.toString().toInt()
        val surface: Int = binding.TIETPropertySurface.text.toString().toInt()
        val numberOfRooms: Int = binding.TIETPropertyNbRooms.text.toString().toInt()
        val numberOfBedrooms: Int = binding.TIETPropertyNbBathrooms.text.toString().toInt()
        val numberOfBathrooms: Int = binding.TIETPropertyNbBathrooms.text.toString().toInt()
        val description: String = binding.TIETPropertyDescription.text.toString()
        val photo: List<String> = listPhoto
        val address: String = binding.TIETPropertyAddress.text.toString()
        val city: String = binding.TIETPropertyCity.text.toString()
        val neighbourhood: String = binding.TIETPropertyNeighbourhood.text.toString()
        val saleStatus: Boolean = binding.toggleSaleStatus.isChecked
        val onTheMarketSince: Long =
            Utils.stringDateToLong(binding.TIETPropertyOnMarketSince.text.toString())
        val offTheMarketSince: Long? =
            if (!binding.TIETPropertyOffMarketSince.text.isNullOrEmpty()) Utils.stringDateToLong(
                binding.TIETPropertyOffMarketSince.text.toString()
            ) else null
        val agentManagingPropertyId: String =
            viewModel.getAgentID(binding.autoCompleteTextViewAgentManagingProperty.text.toString())
        val closeToSchool: Boolean = isCloseToSchool
        val closeToShops: Boolean = isCloseToShops
        val closeToPark: Boolean = isCloseToPark
        val closeToTransport: Boolean = isCloseToTransport
        val listInterest: List<String> = listInterestID

        return Property(
            id = id,
            type = type,
            price = price,
            surface = surface,
            numberOfRooms = numberOfRooms,
            numberOfBedrooms = numberOfBedrooms,
            numberOfBathrooms = numberOfBathrooms,
            description = description,
            photoIDList = photo,
            address = address,
            city = city,
            latLng = latLng,
            neighbourhood = neighbourhood,
            saleStatus = saleStatus,
            onTheMarketSince = onTheMarketSince,
            offTheMarketSince = offTheMarketSince,
            agentManagingPropertyId = agentManagingPropertyId,
            closeToSchool = closeToSchool,
            closeToPark = closeToPark,
            closeToShops = closeToShops,
            closeToTransport = closeToTransport,
            listOfInterest = listInterest,
            listPlaceDetail = listPlaceDetail
        )
    }

    private fun bindButtonSave() {
        if (args.property != null) binding.btnSave.text = "Update"
        binding.btnSave.setOnClickListener {
            saveProperty()
        }
    }

    private fun saveProperty() {
        if (checkIfPropertyCanBeSaved()) {
            val property: Property = createProperty()

            if (args.property != null) viewModel.updateProperty(property) else viewModel.insertProperty(property)

            val service = PropertyNotificationService(requireContext())
            service.showNewPropertyNotification(property)

            val action = CreateModifyFragmentDirections.actionCreateModifyFragmentToPropertyListFragment()
            navController.navigate(action)

        } else {
            Utils.snackBarMaker(binding.root, getString(R.string.fill_all_field))
        }
    }

    private fun checkIfPropertyCanBeSaved(): Boolean {
        return binding.autoCompleteTextViewType.text.toString() != getString(R.string.property_type) &&
                binding.TIETPropertyPrice.text.toString() != "" &&
                binding.TIETPropertySurface.text.toString() != "" &&
                binding.TIETPropertyNbRooms.text.toString() != "" &&
                binding.TIETPropertyNbBathrooms.text.toString() != "" &&
                binding.TIETPropertyNbBathrooms.text.toString() != "" &&
                binding.TIETPropertyDescription.text.toString() != "" &&
                listPhoto.size != 0 &&
                binding.TIETPropertyAddress.text.toString() != "" &&
                binding.TIETPropertyCity.text.toString() != "" &&
                binding.TIETPropertyNeighbourhood.text.toString() != "" &&
                binding.TIETPropertyOnMarketSince.text.toString() != ""
    }

}