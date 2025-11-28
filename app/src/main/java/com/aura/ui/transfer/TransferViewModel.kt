package com.aura.ui.transfer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.domain.model.MoneyTransfer
import com.aura.domain.usecase.TransferMoneyUseCase
import com.aura.ui.model.UiStateWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

/**
 * --- Dependencies ---
 */

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val transferMoneyUseCase: TransferMoneyUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /**
     * --- ui form state & state (loading, success, error) ---
     */

    private val _transferFormState = MutableStateFlow(TransferFormState())
    val transferFormState: StateFlow<TransferFormState> = _transferFormState.asStateFlow()


    private val _transferState = MutableStateFlow<UiStateWrapper<Unit>>(UiStateWrapper.Idle)
    val transferState: StateFlow<UiStateWrapper<Unit>> = _transferState.asStateFlow()

    private var senderId: String = ""

    /**
     * --- Initialization ---
     */

    fun setSenderId(id: String) {
        senderId = id
    }

    /**
     * --- Form updates ---
     */
    fun onRecipientChange(newRecipient: String) {
        _transferFormState.update { currentState ->
            currentState.copy(
                recipient = newRecipient,
                isButtonEnabled = isFormValid(newRecipient, currentState.amount)
            )
        }
    }

    fun onAmountChange(newAmount: String) {
        _transferFormState.update { currentState ->
            currentState.copy(
                amount = newAmount,
                isButtonEnabled = isFormValid(currentState.recipient, newAmount)
            )
        }
    }

    private fun isFormValid(recipient: String, amount: String): Boolean {

        if (recipient.isBlank() || amount.isBlank()) return false
        val amountDouble = amount.toDoubleOrNull() ?: return false
        return amountDouble > 0
    }

    /**
     * --- Business Actions ---
     */

    fun transfer() {

        showLoading()

        viewModelScope.launch {
            try {
                val transfer = buildTransfer()
                val success = transferMoneyUseCase(transfer)

                if (success) {
                    showSuccess()
                } else {
                    showError(R.string.error_transfer_failed)
                }
            } catch (ex: HttpException) {
                val errorMsg = when (ex.code()) {
                    500 -> R.string.error_invalid_recipient
                    else -> R.string.error_transfer_failed
                }
                showError(errorMsg)

            } catch (e: Exception) {
                showError(R.string.error_network)
            }
        }
    }

    private fun buildTransfer(): MoneyTransfer {
        return MoneyTransfer(
            senderId = senderId,
            recipientId = _transferFormState.value.recipient.trim(),
            amount = _transferFormState.value.amount.toDouble()
        )
    }

    /**
     * --- helpers ---
     */
        private fun showLoading() {
            _transferState.value = UiStateWrapper.Loading
        }

        private fun showSuccess() {
            _transferState.value = UiStateWrapper.Success(Unit)
        }

        private fun showError(resId: Int) {
            _transferState.value = UiStateWrapper.Error(context.getString(resId))
        }

}