package com.aura.ui.transfer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.R
import com.aura.data.repository.AuraRepository
import com.aura.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val repository: AuraRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _transferData = MutableStateFlow(TransferData())
    val transferData: StateFlow<TransferData> = _transferData.asStateFlow()

    private val _transferState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val transferState: StateFlow<UiState<Unit>> = _transferState.asStateFlow()

    private var senderId: String = ""

    fun setSenderId(id: String) {
        senderId = id
    }

    fun onRecipientChange(newRecipient: String) {
        _transferData.update { currentState ->
            currentState.copy(
                recipient = newRecipient,
                isButtonEnabled = isFormValid(newRecipient, currentState.amount)
            )
        }
    }

    fun onAmountChange(newAmount: String) {
        _transferData.update { currentState ->
            currentState.copy(
                amount = newAmount,
                isButtonEnabled = isFormValid(currentState.recipient, newAmount)
            )
        }
    }

    private fun isFormValid(recipient: String, amount: String): Boolean {
        // Vérifie que les champs ne sont pas vides et que le montant est valide
        if (recipient.isBlank() || amount.isBlank()) return false

        // Vérifie que le montant est un nombre valide
        val amountDouble = amount.toDoubleOrNull() ?: return false

        // Vérifie que le montant est positif
        return amountDouble > 0
    }

    fun transfer() {
        _transferState.value = UiState.Loading

        viewModelScope.launch {
            // TODO: RETIRER EN PRODUCTION - Délai de test pour le ProgressBar
            kotlinx.coroutines.delay(2000)
            try {
                val amountDouble = _transferData.value.amount.toDoubleOrNull() ?: 0.0

                val success = repository.transfer(
                    sender = senderId,
                    recipient = _transferData.value.recipient,
                    amount = amountDouble
                )
                android.util.Log.d("TransferViewModel", "Transfer result: $success")

                if (success) {
                    _transferState.value = UiState.Success(Unit)
                } else {
                    _transferState.value =
                        UiState.Error(context.getString(R.string.error_transfer_failed))
                }
            } catch (e: HttpException) {
                // Erreur HTTP spécifique (400, 500, etc.)
                android.util.Log.e("TransferViewModel", "HTTP error: ${e.code()}", e)

                val errorMessage = when (e.code()) {
                    500 -> context.getString(R.string.error_invalid_recipient)
                    else -> context.getString(R.string.error_transfer_failed)
                }

                _transferState.value = UiState.Error(errorMessage)
            } catch (e: Exception) {
                // Erreur réseau ou autre
                android.util.Log.e("TransferViewModel", "Transfer exception", e)
                _transferState.value = UiState.Error(context.getString(R.string.error_network))
            }
        }
    }
}