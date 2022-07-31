package com.example.realestatemanager.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.viewmodel.MyAccountViewModel
import com.example.realestatemanager.databinding.ActivityMyAccountBinding
import com.example.realestatemanager.domain.Utils
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyAccountBinding
    lateinit var viewModel: MyAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupViewModel()

        setupButtons()

        observeCurrentUser()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flSettings, SettingsFragment())
            .commit()

    }

    /**
     * ViewModel setup
     */

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MyAccountViewModel::class.java]
    }

    /**
     * Observe agent
     */

    private fun observeCurrentUser() {
        viewModel.currentUser.observe(this, this::updateViewsWithAgent)
    }


    /**
     * Setup buttons
     */

    private fun setupButtons() {
        setupTextButtonDisconnectOrConnect()

        binding.buttonConnect.setOnClickListener {
            signInOrDisconnectButtonClicked()
            if (binding.buttonConnect.text == getString(R.string.connect)) {
                binding.buttonConnect.text = getString(R.string.disconnect)
            } else {
                binding.buttonConnect.text = getString(R.string.connect)
            }
        }

        binding.buttonUpdate.setOnClickListener {
            if (viewModel.getCurrentUser() == null) Utils.snackBarMaker(
                binding.myAccountActivity,
                getString(R.string.unable_to_proceed)
            ) else Utils.snackBarMaker(
                binding.myAccountActivity,
                getString(R.string.update_button_warning)
            )
        }

        binding.buttonDeleteAccount.setOnClickListener {
            if (viewModel.getCurrentUser() == null) Utils.snackBarMaker(
                binding.myAccountActivity,
                getString(R.string.unable_to_proceed)
            ) else viewModel.deleteAgent(this)
        }
    }

    private fun signInOrDisconnectButtonClicked() {
        if (isUserConnected() != null) disconnectUser() else connectUser()
        setupTextButtonDisconnectOrConnect()
    }

    /**
     * SignIn settings
     */

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build()
    )

    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .setIsSmartLockEnabled(false, true)
        .build()

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        Log.d(TAG, "onSignInResult: idpResponse ${result.resultCode}")
        if (result.resultCode == RESULT_OK) {
            setupTextButtonDisconnectOrConnect()
            val agent: FirebaseUser? = viewModel.getCurrentUser()
            Log.d(TAG, "onSignInResult: ${agent!!.displayName}")
            val newAgent = Agent(
                agent.uid,
                agent.displayName.toString(),
                agent.email.toString(),
                agent.photoUrl.toString()
            )
            viewModel.createAgent(newAgent)
            Utils.snackBarMaker(binding.myAccountActivity, getString(R.string.you_are_connected))
        } else {
            Log.e(TAG, "onSignInResult: ${response!!.error!!.message}")
            Utils.snackBarMaker(
                binding.myAccountActivity,
                String.format(getString(R.string.connection_error), response.error?.message)
            )
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

    private fun setupViews() {
        updateViews()
    }

    private fun setupTextButtonDisconnectOrConnect() =
        if (viewModel.getCurrentUser() == null) {
            binding.buttonConnect.text = getText(R.string.connect)
        } else {
            binding.buttonConnect.text = getText(R.string.disconnect)
        }

    private fun updateViews() {
        updateAvatar(null)
        updateName("You are not connected")
        updateEmail("connect")
    }

    private fun updateViewsWithAgent(agent: Agent?) {
        if (agent != null) {
            updateAvatar(agent.picture)
            updateName(agent.name)
            updateEmail(agent.email)
        } else {
            setupViews()
        }
    }

    private fun updateAvatar(photoURL: String?) {
        binding.ivAgentAvatar.load(photoURL) {
            crossfade(true)
            placeholder(R.drawable.agent_placeholder)
            error(R.drawable.agent_placeholder)
            transformations(CircleCropTransformation())
        }
    }

    private fun updateName(agentName: String) {
        binding.tvNameAgent.text = agentName
    }

    private fun updateEmail(agentEmail: String) {
        binding.tvEmailAgent.text = agentEmail
    }

    /**
     * Settings fragment
     */

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val currencySwitch: SwitchPreferenceCompat? = findPreference("currency_preference")
            val notificationSwitch: SwitchPreferenceCompat? = findPreference("notification_preference")

        }

        private fun setCurrencySwitch() {}

        private fun setNotificationSwitch() {}

    }
}