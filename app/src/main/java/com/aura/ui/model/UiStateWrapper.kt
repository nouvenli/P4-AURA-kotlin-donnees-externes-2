package com.aura.ui.model

sealed class UiStateWrapper<out T> {
    object Idle : UiStateWrapper<Nothing>()
    object Loading : UiStateWrapper<Nothing>()
    data class Success<T>(val data: T) : UiStateWrapper<T>()
    data class Error(val message: String) : UiStateWrapper<Nothing>()
}