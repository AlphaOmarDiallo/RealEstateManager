package com.example.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState


class MapsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                GoogleMap(
                    modifier = Modifier.size(300.dp)
                ) {
                    val addressLatLng = LatLng(1.35, 103.87)
                    Marker(
                        state = MarkerState(position = addressLatLng),
                        title = "Singapore",
                        snippet = "Marker in Singapore"
                    )
                }
            }
        }
    }
    

}