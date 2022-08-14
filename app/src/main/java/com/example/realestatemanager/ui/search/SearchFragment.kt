package com.example.realestatemanager.ui.search

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.model.SearchResultObject
import com.example.realestatemanager.data.viewmodel.SearchViewModel
import com.example.realestatemanager.databinding.FragmentSearchBinding
import com.example.realestatemanager.domain.utils.Utils

private lateinit var binding: FragmentSearchBinding
private lateinit var navController: NavController
private lateinit var viewModel: SearchViewModel
private var listProperties: List<Property>? = listOf()
private val cityList: MutableList<String> = mutableListOf()

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getListProperties()
        setupOnClick()
        filteredList()
    }

    private fun setViews() {
        if (listProperties.isNullOrEmpty()) {
            Log.i(TAG, "setViews: empty or null")
        } else {
            if (this.isAdded) {
                setCity()
                setType()
            }
        }
    }

    private fun setType() {
        val listType = mutableListOf<String>()

        for (item in listProperties!!) {
            if (listType.contains(item.type)) Log.i(
                TAG,
                "setType: already in list"
            ) else listType.add(item.type)
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, listType)
        (binding.TILPropertyTypeS.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setCity() {

        for (item in listProperties!!) {
            if (cityList.contains(item.city)) Log.i(
                TAG,
                "setCity: already in list"
            ) else cityList.add(item.city)
        }

        setCityAdapter()
    }

    private fun setCityAdapter() {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, cityList)
        (binding.TILPropertyCityS.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        setCityListener()
    }

    private fun setCityListener() {
        binding.autoCompleteTextViewCityS.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->
                val currentCity = cityList[i]
                binding.autoCompleteTextViewNeiS.setText("")
                setNeighbourHood(currentCity)
            }
    }

    private fun setNeighbourHood(city: String) {
        val neiList: MutableList<String> = mutableListOf()

        for (item in listProperties!!) {
            if (item.city == city) {
                if (neiList.contains(item.neighbourhood)) Log.i(
                    TAG,
                    "setCity: already in list"
                ) else neiList.add(item.neighbourhood)
            }
        }

        setNeighbourHoodAdapter(neiList)

    }

    private fun setNeighbourHoodAdapter(list: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, list)
        (binding.TILPropertyNeiS.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    /**
     *Property list
     */

    private fun getListProperties() {
        viewModel.getPropertyList()
        viewModel.propertyList.observe(requireActivity()) {
            listProperties = it
            if (!it.isNullOrEmpty()) {
                listProperties = it
                setViews()
            }
        }
    }

    /**
     * Search
     */

    private fun setupOnClick() {
        binding.btnSearch.setOnClickListener {
            collectDataToSearchFun(
                if (binding.autoCompleteTextViewTypeS.text.toString() != "") binding.autoCompleteTextViewTypeS.text.toString() else null,
                if (binding.autoCompleteTextViewCityS.text.toString() != "") binding.autoCompleteTextViewCityS.toString() else null,
                if (binding.autoCompleteTextViewNeiS.text.toString() != "") binding.autoCompleteTextViewNeiS.text.toString() else null,
                binding.switch1.isChecked,
                binding.switch2.isChecked,
                binding.switch3.isChecked,
                binding.switch4.isChecked,
                binding.switch5.isChecked,
                if (binding.tiedPriceFrom.text.toString() != "") binding.tiedPriceFrom.text.toString()
                    .toInt() else null,
                if (binding.tiedPriceUpTo.text.toString() != "") binding.tiedPriceUpTo.text.toString()
                    .toInt() else null,
                if (binding.tiedSizeFrom.text.toString() != "") binding.tiedSizeFrom.text.toString()
                    .toInt() else null,
                if (binding.tiedSizeTo.text.toString() != "") binding.tiedSizeTo.text.toString()
                    .toInt() else null,
                binding.toggleNbPicture.isChecked
            )
        }
    }

    private fun collectDataToSearchFun(
        type: String?,
        city: String?,
        neighbourhood: String?,
        school: Boolean,
        shops: Boolean,
        parks: Boolean,
        soldLast3Month: Boolean,
        addedLess7Days: Boolean,
        startingPrice: Int?,
        priceLimit: Int?,
        sizeFrom: Int?,
        sizeUpTo: Int?,
        numberOfPhoto: Boolean
    ) {
        viewModel.searchUserCriteria(
            type,
            city,
            neighbourhood,
            school,
            shops,
            parks,
            soldLast3Month,
            addedLess7Days,
            startingPrice,
            priceLimit,
            sizeFrom,
            sizeUpTo,
            numberOfPhoto
        )
    }

    private fun filteredList() {
        viewModel.filteredList.observe(requireActivity()) {
            Log.e(TAG, "filteredList: ${it.size}")
            btnSeeResultsVisible(it.size, it)
        }
    }

    private fun btnSeeResultsVisible(result: Int, list: List<Property>) {
        Log.e(TAG, "btnSeeResultsVisible: here")
        if (result == 0) {
            binding.btnSeeResult.visibility = View.INVISIBLE
            Utils.snackBarMaker(binding.root, getString(R.string.no_results))
        } else {
            binding.btnSeeResult.visibility = View.VISIBLE
            val searchResults = SearchResultObject(list)
            binding.btnSeeResult.setOnClickListener {
                val action =
                    SearchFragmentDirections.actionSearchFragmentToSearchResults(searchResults)
                navController.navigate(action)
            }
        }
    }

}