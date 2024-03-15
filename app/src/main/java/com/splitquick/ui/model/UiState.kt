package com.splitquick.ui.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface UiState {
    data class Success<T>(val data: T) : UiState
    data class Error(val error: String) : UiState
    object Loading : UiState
}

fun <T> Flow<T>.toUiState(): Flow<UiState> {
    return this.map { data ->
        UiState.Success(data)
    }.onStart {
        UiState.Loading
    }.catch { error ->
        UiState.Error(error.message ?: "Error")
    }
}