package com.aura.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.R
import com.aura.databinding.ActivityHomeBinding
import com.aura.ui.model.UiStateWrapper
import com.aura.ui.transfer.TransferActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    /**
     *     --- view binding and view model ---
     */

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    /**
     * --- lifecycle ---
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("USER_ID") ?: ""
        viewModel.setUserId(userId)

        setupObservers()
        setupListeners()
    }

    /**
     * --- ui observers ---
     */

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }
    }

    /**
     * --- ui listeners ---
     */

    private fun setupListeners() {
        binding.transfer.setOnClickListener {
            val intent = Intent(this@HomeActivity, TransferActivity::class.java)
            intent.putExtra("USER_ID", viewModel.currentUserId)
            startTransferActivityForResult.launch(intent)
        }

        binding.btRetryButton.setOnClickListener {
            viewModel.loadBalance()
        }
    }

    private val startTransferActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                viewModel.loadBalance()
            }
        }


    /**
     * --- ui functions ---
     */

    private fun updateUI(state: UiStateWrapper<HomeFormState>) {

        binding.pbLoading.show(state is UiStateWrapper.Loading || state is UiStateWrapper.Idle)
        binding.tvErrorMessage.show(state is UiStateWrapper.Error)
        binding.btRetryButton.show(state is UiStateWrapper.Error)
        binding.title.show(state is UiStateWrapper.Success)
        binding.balance.show(state is UiStateWrapper.Success)
        binding.transfer.show(state is UiStateWrapper.Success)

        if (state is UiStateWrapper.Success) {
            binding.balance.text = state.data.balanceFormatted
        }
        if (state is UiStateWrapper.Error) {
            binding.tvErrorMessage.text = state.message
        }
    }

    /**
     * --- system callbacks  ---
     */
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
/**
 * --- extensions ---
 */

private fun View.show(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}