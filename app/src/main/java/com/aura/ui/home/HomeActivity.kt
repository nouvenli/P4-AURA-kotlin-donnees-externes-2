package com.aura.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.R
import com.aura.databinding.ActivityHomeBinding
import com.aura.ui.common.UiState
import com.aura.ui.transfer.TransferActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private var userId: String = ""

    private val startTransferActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Transfert réussi, on recharge le solde
                viewModel.loadBalance(userId)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Récupérer l'ID utilisateur
        userId = intent.getStringExtra("USER_ID") ?: ""

        // Observer l'état du ViewModel
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }

        // Charger le solde
        viewModel.loadBalance(userId)

        // Bouton de transfert
        binding.transfer.setOnClickListener {
            val intent = Intent(this@HomeActivity, TransferActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startTransferActivityForResult.launch(intent)
        }

        // Bouton Réessayer
        binding.btRetryButton.setOnClickListener {
            viewModel.loadBalance(userId)
        }
    }

    private fun updateUI(state: UiState<HomeData>) {
        when (state) {
            is UiState.Idle -> {
                // État initial - afficher un écran vide ou loading
                binding.pbLoading.visibility = View.VISIBLE
                binding.title.visibility = View.GONE
                binding.balance.visibility = View.GONE
                binding.transfer.visibility = View.GONE
                binding.tvErrorMessage.visibility = View.GONE
                binding.btRetryButton.visibility = View.GONE
            }

            is UiState.Loading -> {
                // Mode chargement
                binding.pbLoading.visibility = View.VISIBLE
                binding.title.visibility = View.GONE
                binding.balance.visibility = View.GONE
                binding.transfer.visibility = View.GONE
                binding.tvErrorMessage.visibility = View.GONE
                binding.btRetryButton.visibility = View.GONE
            }

            is UiState.Error -> {
                // Mode erreur
                binding.pbLoading.visibility = View.GONE
                binding.title.visibility = View.GONE
                binding.balance.visibility = View.GONE
                binding.transfer.visibility = View.GONE
                binding.tvErrorMessage.visibility = View.VISIBLE
                binding.tvErrorMessage.text = state.message
                binding.btRetryButton.visibility = View.VISIBLE
            }

            is UiState.Success -> {
                // Mode succès
                binding.pbLoading.visibility = View.GONE
                binding.title.visibility = View.VISIBLE
                binding.balance.visibility = View.VISIBLE
                binding.balance.text = state.data.balanceFormatted
                binding.transfer.visibility = View.VISIBLE
                binding.tvErrorMessage.visibility = View.GONE
                binding.btRetryButton.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.disconnect -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}