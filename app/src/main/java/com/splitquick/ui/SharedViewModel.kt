package com.splitquick.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitquick.domain.Repository
import com.splitquick.domain.model.Group
import com.splitquick.domain.model.Member
import com.splitquick.domain.model.Result
import com.splitquick.domain.model.User
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

    val user = repository.user
    val events = searchEvent.flatMapLatest { filter ->
        repository.filterEvents(filter)
    }

    val groupsUiState = combine<User, List<Group>, GroupsUiState>(
        repository.user,
        searchGroup.flatMapLatest {
            repository.getGroupsByNameWithExpenses(it)
        }
    ) { user, groups ->
        GroupsUiState.Success(user, groups)
    }.catch { error ->
        emit(GroupsUiState.Error(error.message ?: "Error"))
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        GroupsUiState.Loading
    )

    fun saveUser(firstName: String, lastName: String, email: String) {
        viewModelScope.launch {
            repository.saveUser(firstName, lastName, email)
        }
    }

    fun addGroup(group: Group, members: List<Member>): StateFlow<Result> {
        return repository.addGroup(group, members).map<Boolean, Result> {
            Result.Success
        }.catch {
            it.printStackTrace()
            emit(Result.Error(it.message ?: "Error"))
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            Result.Loading
        )
    }

    fun searchGroup(group: String) {
        searchGroup.value = group
    }

    fun filterEvents(filter: String) {
        searchEvent.value = filter
    }

}

sealed interface GroupsUiState {
    data class Success(val user: User, val groups: List<Group>) : GroupsUiState
    data class Error(val error: String) : GroupsUiState
    object Loading : GroupsUiState
}