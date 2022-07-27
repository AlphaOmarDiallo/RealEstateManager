package com.example.realestatemanager.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.data.viewmodel.MyAccountViewModel
import com.example.realestatemanager.databinding.ActivityMyAccountBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class MyAccountActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyAccountBinding
    lateinit var viewModel: MyAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupViewModel()

    }

    /**
     * ViewModel setup
     */

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MyAccountViewModel::class.java]
    }

    /**
     * Sign in or disconnect button click
     */

    private fun signInOrDisconnectButtonClicked() =
        if (isUserConnected() != null) disconnectUser() else connectUser()

    /**
     * SignIn settings
     */

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build())

    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...
        } else {
            Log.e(TAG, "onSignInResult: ${response!!.error!!.message}")
        }
    }

    /**
     * Account management methods
     */

    private fun isUserConnected() = viewModel.getCurrentUser()

    private fun connectUser() = signInLauncher.launch(signInIntent)

    private fun disconnectUser() = viewModel.disconnectUser(this)
}