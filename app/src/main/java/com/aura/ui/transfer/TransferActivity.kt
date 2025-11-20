package com.aura.ui.transfer

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.aura.R
import com.aura.databinding.ActivityTransferBinding
import com.aura.ui.common.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransferBinding
    private val viewModel: TransferViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Récupérer l'ID de l'expéditeur
        val senderId = intent.getStringExtra("USER_ID") ?: ""
        viewModel.setSenderId(senderId)

        configObservers()
        configListeners()
    }

    private fun configObservers() {
        // Observer les données du formulaire
        lifecycleScope.launch {
            viewModel.transferData.collect { data ->
                // vérifier si le transfert est en cours (pour reactiver le bouton) et mettre à jour l'état du bouton
                val isLoading = viewModel.transferState.value is UiState.Loading
                binding.transfer.isEnabled = data.isButtonEnabled && !isLoading
            }
        }

        // Observer l'état du transfert
        lifecycleScope.launch {
            viewModel.transferState.collect { state ->
                when (state) {
                    is UiState.Idle -> {
                        binding.loading.visibility = View.GONE
                    }

                    is UiState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.transfer.isEnabled = false
                    }

                    is UiState.Success -> {
                        binding.loading.visibility = View.GONE
                        // Afficher le message de succès
                        Toast.makeText(
                            this@TransferActivity,
                            getString(R.string.transfer_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        // Transfert réussi - retour à Home
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    is UiState.Error -> {
                        binding.loading.visibility = View.GONE
                        Toast.makeText(
                            this@TransferActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        // Réactiver le bouton
                        binding.transfer.isEnabled = viewModel.transferData.value.isButtonEnabled
                    }
                }
            }
        }
    }

    private fun configListeners() {
        binding.recipient.addTextChangedListener {
            viewModel.onRecipientChange(it.toString())
        }

        binding.amount.addTextChangedListener {
            viewModel.onAmountChange(it.toString())
        }

        binding.transfer.setOnClickListener {
            viewModel.transfer()
        }
    }
}