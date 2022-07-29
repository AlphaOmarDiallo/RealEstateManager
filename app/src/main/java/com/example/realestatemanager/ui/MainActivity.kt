package com.example.realestatemanager.ui

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.R
import com.example.realestatemanager.data.viewmodel.MainViewModel
import com.example.realestatemanager.databinding.ActivityMainBinding
import com.example.realestatemanager.domain.Constant
import com.example.realestatemanager.domain.Utils
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var toggle: ActionBarDrawerToggle
    private var euroToDollar by Delegates.notNull<Double>()
    private var dollarToEuro by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupUI()

        setupViewModel()

        checkLocationPermission()

        checkConnectivity(this)

        observeRates()
    }

    /**
     * UI update methods
     */

    private fun setupUI() {
        setupToolBar()
        setupNavDrawer()
    }

    private fun setupToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
    }

    /**
     * Navigation drawer setup
     */
    private fun setupNavDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.mainLayout,
            R.string.Open_drawer_menu,
            R.string.Close_drawer_menu
        )
        binding.mainLayout.addDrawerListener(toggle)
        toggle.syncState()
        handleClickOnNavDrawer()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun handleClickOnNavDrawer() {
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.MyAccountNavDrawer -> openMyAccountActivity()
                R.id.MorgageCalcNacDrawer -> openMyMortgageCalculatorActivity()
                else -> Log.w(TAG, "handleClickOnNavDrawer: error")
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun openMyAccountActivity() {
        val intent = Intent(this, MyAccountActivity::class.java)
        startActivity(intent)
    }

    private fun openMyMortgageCalculatorActivity() {
        val intent = Intent(this, MortgageCalculatorActivity::class.java)
        startActivity(intent)
    }

    /**
     * ViewModel setup
     */
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    /**
     * Check connectivity
     */

    private fun checkConnectivity(context: Context) {
        viewModel.checkConnectivity(context)
            .observe(this, this::showInternetConnectionStatusMessage)
    }

    private fun showInternetConnectionStatusMessage(connected: Boolean) {
        if (connected) {
            binding.tvInternetStatusMA.text = ""
        } else {
            binding.tvInternetStatusMA.text = getText(R.string.not_connected_to_internet)
        }
    }

    /**
     * Updating rates qnd storing new values in data store to make it available in the all application
     */
    private fun observeRates() {
        viewModel.usdRate.observe(this) {
            updatingDollarToEuroRate(it)
            Log.e(TAG, "observeRates: $it")
        }
        viewModel.eurRate.observe(this) {
            updatingEuroToDollarRate(it)
            Log.e(TAG, "observeRates: $it")
        }
    }

    private fun updatingEuroToDollarRate(rate: Double) {
        Log.e(TAG, "updatingEuroToDollarRate: $rate")
        viewModel.saveEuroToDollarRateToDataStore(rate)
        euroToDollar = rate
    }

    private fun updatingDollarToEuroRate(rate: Double) {
        viewModel.saveDollarToEuroRateToDataStore(rate)
        dollarToEuro = rate
        Log.e(TAG, "updatingEuroToDollarRate: $rate")
    }

    /**
     * Permission management
     */

    private fun checkLocationPermission() {
        if (hasRequiredPermissionsToCheckLocation()) Log.i(
            TAG,
            "checkLocationPermission: location permission granted"
        ) else requestPermissionsToCheckLocation()
    }

    private fun hasRequiredPermissionsToCheckLocation() =
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestPermissionsToCheckLocation() =
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.connectivity_rational),
            Constant.PERMISSION_CONNECTIVITY_REQUEST_CODE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            requestPermissionsToCheckLocation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Utils.snackBarMaker(binding.root, "Location permissions granted")
        Utils.isInternetAvailableBuildVersionCodAboveM(this)
    }
}