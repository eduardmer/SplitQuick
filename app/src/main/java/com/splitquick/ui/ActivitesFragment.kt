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
import androidx.recyclerview.widget.LinearLayoutManager
import com.splitquick.R
import com.splitquick.databinding.FragmentActivitiesBinding
import com.splitquick.domain.model.Event
import com.splitquick.ui.adapter.EventsAdapter
import com.splitquick.ui.model.UiState
import com.splitquick.utils.filterData
import com.splitquick.utils.launchAndRepeatWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ActivitesFragment : Fragment() {

    private lateinit var binding: FragmentActivitiesBinding
    private val viewModel by viewModels<SharedViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = EventsAdapter(requireContext())
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            (topAppBar.menu.findItem(R.id.search).actionView as SearchView).filterData { eventName ->
                viewModel.filterEvents(eventName)
            }
        }
        launchAndRepeatWithLifecycle {
            viewModel.events.collectLatest { uiState ->
                when(uiState) {
                    is UiState.Success<*> -> {
                        val events = uiState.data as List<Event>
                        binding.noActivityText.isVisible = events.isEmpty()
                        adapter.submitList(events)
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