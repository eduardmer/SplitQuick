package com.splitquick.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
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
import com.splitquick.utils.DecimalDigitsInputFilter
import com.splitquick.utils.content
import com.splitquick.utils.isEmpty
import com.splitquick.utils.launchAndRepeatWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

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
        binding.apply {
            groupNameText.threshold = 1
            groupNameText.setAdapter(groupsAdapter)
            memberSpinner.adapter = membersAdapter
            groupNameText.setOnItemClickListener { _, _, position, _ ->
                selectedGroup = groupsAdapter.getItem(position)
                viewModel.selectGroup(selectedGroup!!)
            }
            memberSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position != 0)
                        selectedMember = membersAdapter.getItem(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
            topAppBar.setOnMenuItemClickListener {
                if (
                    selectedGroup == null ||
                    selectedMember == null ||
                    descriptionText.isEmpty() ||
                    amountText.isEmpty() ||
                    amountText.content() == "." ||
                    amountText.content().toDouble() == 0.0
                )
                    Toast.makeText(requireContext(), R.string.add_expenses_not_valid, Toast.LENGTH_SHORT).show()
                else
                    viewModel.addExpense(
                        Expense(
                            contributorId = selectedMember!!.id,
                            contributorName = "${selectedMember!!.firstName} ${selectedMember!!.lastName}",
                            groupId = selectedGroup!!.id,
                            groupName = selectedGroup!!.name,
                            description = descriptionText.content(),
                            amount = BigDecimal(amountText.content()),
                            currency = selectedGroup!!.currency,
                            date = System.currentTimeMillis()
                        )
                    )
                false
            }
        }
        launchAndRepeatWithLifecycle {
            viewModel.uiState.collect { uiState ->
                groupsAdapter.updateData(uiState.groups)
                membersAdapter.updateData(uiState.members)
                showProgressBar(uiState.isLoading)
                binding.amountContainer.startIconDrawable = AppCompatResources.getDrawable(requireContext(), uiState.selectedGroup.currency.icon)
                binding.amountText.filters = arrayOf(DecimalDigitsInputFilter(uiState.selectedGroup.currency))
                if (uiState.error != null)
                    Toast.makeText(requireContext(), uiState.error, Toast.LENGTH_SHORT).show()
                if (uiState.goBack)
                    findNavController().navigateUp()
            }
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

}