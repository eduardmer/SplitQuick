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
import com.splitquick.domain.model.Group
import com.splitquick.domain.model.Member
import com.splitquick.domain.model.Result
import com.splitquick.ui.adapter.AddMemberAdapter
import com.splitquick.utils.content
import com.splitquick.utils.isEmpty
import com.splitquick.utils.launchAndRepeatWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class CreateGroupFragment : Fragment() {

    private lateinit var binding: FragmentCreateGroupBinding
    private val viewModel: SharedViewModel by viewModels()
    private val members = mutableListOf(Member(), Member())
    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val membersAdapter = AddMemberAdapter(members)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = membersAdapter
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.done) {
                job?.cancel()
                if (
                    binding.fullNameText.isEmpty() ||
                    members.any { it.firstName.trim().isEmpty() || it.lastName.trim().isEmpty() }
                )
                    Toast.makeText(requireContext(), R.string.group_members_not_valid, Toast.LENGTH_SHORT).show()
                else
                    job = launchAndRepeatWithLifecycle {
                        viewModel.addGroup(
                            Group(
                                name = binding.fullNameText.content(),
                                date = System.currentTimeMillis()
                            ),
                            members
                        ).collect { result ->
                            when(result) {
                                Result.Success -> {
                                    showProgressBar(false)
                                    findNavController().navigateUp()
                                }
                                is Result.Error -> {
                                    showProgressBar(false)
                                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                                }
                                Result.Loading -> showProgressBar(true)
                            }
                        }
                    }
            }
            false
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

}