package com.splitquick.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitquick.domain.Repository
import com.splitquick.ui.model.UiState
import com.splitquick.ui.model.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    val configurations = repository.isDarkModeEnabled.toUiState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UiState.Loading
    )

}