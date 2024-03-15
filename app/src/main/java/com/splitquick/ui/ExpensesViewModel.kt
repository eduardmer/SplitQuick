package com.splitquick.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitquick.R
import com.splitquick.domain.Repository
import com.splitquick.domain.model.Expense
import com.splitquick.domain.model.Group
import com.splitquick.domain.model.Member
import com.splitquick.ui.model.ItemType
import com.splitquick.ui.model.Payments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    private val selectedGroup = MutableStateFlow(Group(id = -1))

    private val _uiState = MutableStateFlow(ExpensesUiState(isLoading = true))
    val uiState = combine(
        repository.getGroupsByName(""),
        selectedGroup.flatMapLatest {
            repository.getMembersByGroup(it.id)
        },
        _uiState
    ) { groups, members, state ->
        state.copy(
            groups = groups,
            selectedGroup = selectedGroup.value,
            members = members,
            isLoading = false
        )
    }.catch {
        emit(_uiState.value.copy(error = it.message ?: "Error", isLoading = false))
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        _uiState.value
    )

    val expensesState = savedStateHandle.getStateFlow<Long>("groupId", -1).flatMapLatest { groupId ->
        repository.getCalculations(groupId).zip(repository.getAllExpensesForGroup(groupId)) { calculations, payments ->
            val items = mutableListOf<Payments>()
            items.add(Payments(type = ItemType.TITLE, title = R.string.group_members))
            items.addAll(calculations.map {
                Payments(type = ItemType.CALCULATION, contributorName = it.giverName, calculation = it.amount, currency = it.currency)
            })
            items.add(Payments(type = ItemType.TITLE, R.string.history))
            items.addAll(payments.map {
                Payments(type = ItemType.HISTORY, contributorName = it.contributorName, description = it.description, contributedAmount = it.amount, currency = it.currency)
            })
            items
        }
    }

    fun selectGroup(group: Group) {
        selectedGroup.value = group
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                repository.addExpense(expense)
                _uiState.update {
                    it.copy(goBack = true)
                }
            } catch (error: Throwable) {
                error.printStackTrace()
                _uiState.update {
                    it.copy(error = error.message ?: "Error")
                }
            }
        }
    }

}

data class ExpensesUiState(
    val groups: List<Group> = emptyList(),
    val selectedGroup: Group = Group(id = -1),
    val members: List<Member> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false,
    val goBack: Boolean = false
)