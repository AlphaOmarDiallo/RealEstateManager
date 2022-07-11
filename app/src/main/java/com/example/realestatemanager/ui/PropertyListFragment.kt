package com.example.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.example.realestatemanager.ui.ui.theme.RealEstateManagerTheme

class PropertyListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Test("Alpha")
            }
        }
    }
    
    @Composable
    fun MyApp(){
        RealEstateManagerTheme() {
            
        }
    }
    
    @Composable
    fun Test(name: String){
        Text(text = "Bonjour $name")
    }

    @Preview(showBackground = true)
    @Composable
    fun FragmentPreview(){
        RealEstateManagerTheme {
            Test(name = "Android")
        }
    }

}