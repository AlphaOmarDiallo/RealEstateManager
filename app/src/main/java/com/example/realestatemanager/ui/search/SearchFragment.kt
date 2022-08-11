package com.example.realestatemanager.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.viewmodel.SearchViewModel
import com.example.realestatemanager.databinding.FragmentSearchBinding

private lateinit var binding: FragmentSearchBinding
private lateinit var navController: NavController
private lateinit var viewModel: SearchViewModel
private var listProperties: List<Property>? = listOf()

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNumberProperties()

        getListProperties()

    }

    private fun getNumberProperties() {
        val number = 0
        binding.textView.setText("$number")
    }

    private fun setType() {
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
        (binding.TILPropertyTypeS.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

   private fun setCity() {
        val cityList: MutableList<String> = mutableListOf()

            for (item in listProperties!!){
                cityList.add(item.city)
            }

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, cityList)
        (binding.TILPropertyTypeS.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun getListProperties() {
        viewModel.propertyList.observe(requireActivity()){
            listProperties = it
            setViews(it)
        }
    }

    private fun setViews(list: List<Property>){
        if (list.isNotEmpty()) {
            setCity()
            setType()
        }
    }


}