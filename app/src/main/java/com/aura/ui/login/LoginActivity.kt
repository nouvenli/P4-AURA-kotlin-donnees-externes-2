package com.aura.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityLoginBinding
import com.aura.ui.model.UiStateWrapper
import com.aura.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    /**
     *     --- view binding and view model ---
      */

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    /**
     * --- lifecycle ---
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()

    }

    /**
     * --- ui observers ---
     */

    private fun setupObservers() {

        // Form state
        lifecycleScope.launch {
            viewModel.loginFormState.collect { stateData ->
                binding.login.isEnabled = stateData.isButtonEnabled
            }
        }

        // Login state (loading, success, error)
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->

                updateUI(state)

                when (state) {
                    is UiStateWrapper.Success -> navigateToHome()
                    is UiStateWrapper.Error -> showError(state.message)
                    else -> Unit
                }
            }
        }
    }

    /**
     * --- ui listeners ---
      */

    private fun setupListeners() {
        binding.identifier.addTextChangedListener { viewModel.onIdentifierChange(it.toString())}
        binding.password.addTextChangedListener { viewModel.onPasswordChange(it.toString())}
        binding.login.setOnClickListener { viewModel.connect() }
    }

    /**
     * --- ui functions ---
     */

    private fun updateUI(state: UiStateWrapper<Unit>) {
        binding.loading.show(state is UiStateWrapper.Loading)
    }

    /**
     * --- navigation ---
      */

    private fun navigateToHome() {
        val userId = viewModel.loginFormState.value.identifier
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
        finish()
    }

    /**
     * --- error handling ---
     */
    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
/**
 * --- extensions ---
 */

private fun View.show(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

