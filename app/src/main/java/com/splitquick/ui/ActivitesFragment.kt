package com.splitquick.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.splitquick.R
import com.splitquick.databinding.FragmentActivitiesBinding
import com.splitquick.ui.adapter.EventsAdapter
import com.splitquick.utils.launchAndRepeatWithLifecycle
import dagger.hilt.android.AndroidEntryPoint

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
        binding.recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        (binding.topAppBar.menu.findItem(R.id.search).actionView as SearchView).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterEvents(newText ?: "")
                return false
            }
        })
        launchAndRepeatWithLifecycle {
            viewModel.events.collect {
                binding.noActivityText.isVisible = it.isEmpty()
                adapter.submitList(it)
            }
        }
    }

}