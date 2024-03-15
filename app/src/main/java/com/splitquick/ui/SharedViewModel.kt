package com.splitquick.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitquick.domain.Repository
import com.splitquick.domain.model.Group
import com.splitquick.domain.model.Member
import com.splitquick.domain.model.Result
import com.splitquick.domain.model.Settings
import com.splitquick.ui.model.GroupsScreenData
import com.splitquick.ui.model.UiState
import com.splitquick.ui.model.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val searchGroup = MutableStateFlow("")
    private val searchEvent = MutableStateFlow("")

    val user = repository.user.toUiState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UiState.Loading
    )

    val groupsUiState = combine(
        repository.user,
        searchGroup.flatMapLatest {
            repository.getGroupsByNameWithExpenses(it)
        }
    ) { user, groups ->
        GroupsScreenData(user, groups)
    }.toUiState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UiState.Loading
    )

    val events = searchEvent.flatMapLatest { filter ->
        repository.filterEvents(filter)
    }.toUiState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UiState.Loading
    )

    val settings = repository.settings.toUiState().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        UiState.Loading
    )

    fun saveUser(firstName: String, lastName: String, email: String) {
        viewModelScope.launch {
            repository.saveUser(firstName, lastName, email)
        }
    }

    fun addGroup(group: Group, members: List<Member>): StateFlow<UiState> {
        return repository.addGroup(group, members).map<Boolean, UiState> {
            UiState.Success(it)
        }.catch {
            it.printStackTrace()
            emit(UiState.Error(it.message ?: "Error"))
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState.Loading
        )
    }

    fun searchGroup(group: String) {
        searchGroup.value = group
    }

    fun filterEvents(filter: String) {
        searchEvent.value = filter
    }

    fun enableDarkMode(isDarkModeEnabled: Boolean) {
        viewModelScope.launch {
            repository.enableDarkMode(isDarkModeEnabled)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            repository.setLanguage(language)
        }
    }

}