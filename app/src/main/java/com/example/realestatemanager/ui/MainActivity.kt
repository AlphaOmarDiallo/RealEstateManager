package com.example.realestatemanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.realestatemanager.R
import com.example.realestatemanager.domain.Utils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utils.isInternetAvailable2(this)
    }
}