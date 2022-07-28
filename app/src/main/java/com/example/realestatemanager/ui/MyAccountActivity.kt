package com.example.realestatemanager.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.viewmodel.MyAccountViewModel
import com.example.realestatemanager.databinding.ActivityMyAccountBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult

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
            val agent = viewModel.getCurrentUser()
            val agentCheck = viewModel.getAgentByIdInDatabase(agent!!.uid)
            if (agentCheck != null) viewModel.setAgent(agentCheck) else viewModel.createAgent(agent)
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

    /**
     * Setup views
     */

    private fun setupButtonDisconnectOrConnect() =
        if (viewModel.getCurrentUser() != null) binding.buttonConnect.text = getText(R.string.connect) else binding.buttonConnect.text = getText(R.string.disconnect)

    private fun observeCurrentUser() =
        viewModel.currentUser?.observe(this, this::updateViewsWithAgent)

    private fun updateViewsWithAgent(agent: Agent){
        updateAvatar(agent.picture)
        updateName(agent.name)
        updateEmail(agent.email)
    }

    private fun updateAvatar(photoURL: String?){
        if (photoURL != null) {
            binding.ivAgentAvatar.load(photoURL)
        } else {
            binding.ivAgentAvatar.load(R.drawable.agent_placeholder)
        }
    }

    private fun updateName(agentName: String){
        binding.tvNameAgent.text = agentName
    }

    private fun updateEmail(agentEmail: String){
        binding.tvEmailAgent.text = agentEmail
    }
}