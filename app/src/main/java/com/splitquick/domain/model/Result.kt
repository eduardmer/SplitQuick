package com.splitquick.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result {
    object Success : Result
    data class Error(val error: String) : Result
    object Loading: Result
}

fun <T> Flow<T>.toResult() = map<T, Result> {
    Result.Success
}.onStart {
    emit(Result.Loading)
}.catch {
    Result.Error(it.message ?: "Error")
}