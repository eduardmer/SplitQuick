package com.splitquick.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.splitquick.R
import com.splitquick.databinding.FragmentAddExpenseBinding
import com.splitquick.domain.model.Expense
import com.splitquick.domain.model.Group
import com.splitquick.domain.model.Member
import com.splitquick.ui.adapter.SelectGroupAdapter
import com.splitquick.ui.adapter.SelectMemberAdapter
import com.splitquick.utils.content
import com.splitquick.utils.isEmpty
import com.splitquick.utils.launchAndRepeatWithLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExpenseFragment : Fragment() {

    private lateinit var binding: FragmentAddExpenseBinding
    private val viewModel: ExpensesViewModel by viewModels()
    private var selectedGroup: Group? = null
    private var selectedMember: Member? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val groupsAdapter = SelectGroupAdapter(requireContext())
        val membersAdapter = SelectMemberAdapter(requireContext(), mutableListOf(Member()))
        binding.groupNameText.apply {
            threshold = 1
            setAdapter(groupsAdapter)
        }
        binding.memberSpinner.adapter = membersAdapter
        launchAndRepeatWithLifecycle {
            viewModel.uiState.collect { uiState ->
                groupsAdapter.updateData(uiState.groups)
                membersAdapter.updateData(uiState.members)
                showProgressBar(uiState.isLoading)
                if (uiState.error != null)
                    Toast.makeText(requireContext(), uiState.error, Toast.LENGTH_SHORT).show()
                if (uiState.goBack)
                    findNavController().navigateUp()
            }
        }
        binding.groupNameText.setOnItemClickListener { _, _, position, _ ->
            selectedGroup = groupsAdapter.getItem(position)
            viewModel.selectGroup(selectedGroup!!.id)
        }
        binding.memberSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0)
                    selectedMember = membersAdapter.getItem(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.done) {
                if (
                    selectedGroup == null ||
                    selectedMember == null ||
                    binding.descriptionText.isEmpty() ||
                    binding.amountText.isEmpty() ||
                    binding.amountText.content() == "." ||
                    binding.amountText.content().toDouble() == 0.0
                )
                    Toast.makeText(requireContext(), R.string.add_expenses_not_valid, Toast.LENGTH_SHORT).show()
                else
                    viewModel.addExpense(
                        Expense(
                            contributorId = selectedMember!!.id,
                            contributorName = "${selectedMember!!.firstName} ${selectedMember!!.lastName}",
                            groupId = selectedGroup!!.id,
                            groupName = selectedGroup!!.name,
                            description = binding.descriptionText.text.toString(),
                            amount = binding.amountText.text.toString().toDouble(),
                            date = System.currentTimeMillis()
                        )
                    )
            }
            true
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

}