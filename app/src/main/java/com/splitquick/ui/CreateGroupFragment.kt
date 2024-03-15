package com.splitquick.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.splitquick.R
import com.splitquick.databinding.FragmentCreateGroupBinding
import com.splitquick.domain.model.Currency
import com.splitquick.domain.model.Group
import com.splitquick.domain.model.Member
import com.splitquick.domain.model.Result
import com.splitquick.ui.adapter.AddMemberAdapter
import com.splitquick.ui.adapter.CurrencyAdapter
import com.splitquick.ui.model.UiState
import com.splitquick.utils.content
import com.splitquick.utils.isEmpty
import com.splitquick.utils.launchAndRepeatWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class CreateGroupFragment : Fragment() {

    private lateinit var binding: FragmentCreateGroupBinding
    private val viewModel: SharedViewModel by viewModels()
    private var selectedCurrency: Currency? = null
    private val members = mutableListOf(Member(), Member())
    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val membersAdapter = AddMemberAdapter(members)
        val currencyAdapter = CurrencyAdapter(requireContext(), Currency.values().asList())
        binding.apply {
            currencyText.setAdapter(currencyAdapter)
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = membersAdapter
            currencyText.setOnItemClickListener { _, _, position, _ ->
                selectedCurrency = currencyAdapter.getItem(position)
            }
            topAppBar.setOnMenuItemClickListener {
                job?.cancel()
                if (
                    binding.fullNameText.isEmpty() ||
                    selectedCurrency == null ||
                    members.any { it.firstName.trim().isEmpty() || it.lastName.trim().isEmpty() }
                )
                    Toast.makeText(requireContext(), R.string.add_expenses_not_valid, Toast.LENGTH_SHORT).show()
                else
                    job = launchAndRepeatWithLifecycle {
                        viewModel.addGroup(
                            Group(
                                name = fullNameText.content(),
                                currency = selectedCurrency!!,
                                date = System.currentTimeMillis()
                            ),
                            members
                        ).collect { result ->
                            when(result) {
                                is UiState.Success<*> -> {
                                    showProgressBar(false)
                                    findNavController().navigateUp()
                                }
                                is UiState.Error -> {
                                    showProgressBar(false)
                                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                                }
                                UiState.Loading -> showProgressBar(true)
                            }
                        }
                    }
                false
            }
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

}