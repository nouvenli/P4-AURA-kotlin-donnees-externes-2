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
import com.aura.ui.model.UiStateWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferActivity : AppCompatActivity() {


    /**
     *    --- view binding and view model ---
     *    */

    private lateinit var binding: ActivityTransferBinding
    private val viewModel: TransferViewModel by viewModels()

    /**
     *    --- lifecycle ---
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val senderId = intent.getStringExtra("USER_ID") ?: ""
        viewModel.setSenderId(senderId)

        setupObservers()
        setupListeners()
    }

    /**
     *    --- ui observers ---
     */

    private fun setupObservers() {

        // Form state
        lifecycleScope.launch {
            viewModel.transferFormState.collect { state ->
                val isLoading = viewModel.transferState.value is UiStateWrapper.Loading
                binding.transfer.isEnabled = state.isButtonEnabled && !isLoading
            }
        }

        // Transfer state (loading, success, error)
        lifecycleScope.launch {
            viewModel.transferState.collect { state ->
                updateUI(state)

                when (state) {

                    is UiStateWrapper.Success -> {
                        Toast.makeText(
                            this@TransferActivity,
                            getString(R.string.transfer_success),
                            Toast.LENGTH_SHORT
                        ).show()

                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    is UiStateWrapper.Error -> {
                        Toast.makeText(
                            this@TransferActivity,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit

                }
            }
        }
    }

    /**
     *    --- ui listeners ---
     */


    private fun setupListeners() {
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

    /**
     *    --- ui functions ---
     */

    private fun updateUI(state: UiStateWrapper<Unit>) {

        binding.loading.show(state is UiStateWrapper.Loading)
        binding.transfer.isEnabled = state !is UiStateWrapper.Loading
    }

}

// --- extensions ---

private fun View.show(show: Boolean) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}
