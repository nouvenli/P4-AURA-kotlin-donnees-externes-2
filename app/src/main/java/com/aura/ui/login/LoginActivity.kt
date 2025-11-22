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
import com.aura.ui.common.UiState
import com.aura.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private fun configObservers() {
        // Observer les données du formulaire
        lifecycleScope.launch {

            viewModel.loginData.collect { data ->
                binding.login.isEnabled = data.isButtonEnabled
            }
        }

        // Observer l'état de connexion
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {

                    is UiState.Idle -> {
                        // État initial - ne rien faire
                        binding.loading.visibility = View.GONE
                    }

                    is UiState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.login.isEnabled = false
                    }

                    is UiState.Success -> {
                        binding.loading.visibility = View.GONE
                        // Connexion réussie
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("USER_ID", viewModel.getUserId())
                        startActivity(intent)
                        finish()
                    }

                    is UiState.Error -> {
                        binding.loading.visibility = View.GONE
                        Toast.makeText(
                            this@LoginActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        configObservers()
        configListeners()

    }

    private fun configListeners() {
        binding.identifier.addTextChangedListener {
            viewModel.onIdentifierChange(it.toString())
        }

        binding.password.addTextChangedListener {
            viewModel.onPasswordChange(it.toString())
        }

        binding.login.setOnClickListener {
            viewModel.connect()
        }
    }
}