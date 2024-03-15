package com.splitquick.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.splitquick.R
import com.splitquick.databinding.FragmentGroupsBinding
import com.splitquick.ui.adapter.GroupsAdapter
import com.splitquick.ui.model.GroupsScreenData
import com.splitquick.ui.model.UiState
import com.splitquick.utils.filterData
import com.splitquick.utils.launchAndRepeatWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GroupsFragment : Fragment() {

    private lateinit var binding: FragmentGroupsBinding
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = GroupsAdapter ({
            findNavController().navigate(GroupsFragmentDirections.actionGroupsFragmentToCreateGroupFragment())
        }, {
            findNavController().navigate(GroupsFragmentDirections.actionGroupsFragmentToCalculationsFragment(it))
        })
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter
            (topAppBar.menu.findItem(R.id.search).actionView as SearchView).filterData { groupName ->
                viewModel.searchGroup(groupName)
            }
            startGroup.setOnClickListener {
                findNavController().navigate(GroupsFragmentDirections.actionGroupsFragmentToCreateGroupFragment())
            }
        }
        launchAndRepeatWithLifecycle {
            viewModel.groupsUiState.collectLatest { uiState ->
                when(uiState) {
                    is UiState.Success<*> -> {
                        val data = uiState.data as GroupsScreenData
                        binding.welcomeText.text = getString(R.string.welcome, data.user.firstName)
                        if (data.groups.isEmpty()) {
                            binding.noGroupText.visibility = View.VISIBLE
                            binding.startGroup.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        } else {
                            binding.noGroupText.visibility = View.GONE
                            binding.startGroup.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                        }
                        adapter.submitList(uiState.data.groups)
                        showProgressBar(false)
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), uiState.error, Toast.LENGTH_SHORT).show()
                        showProgressBar(false)
                    }
                    UiState.Loading -> showProgressBar(true)
                }
            }
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

}