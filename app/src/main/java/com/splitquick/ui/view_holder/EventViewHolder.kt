package com.splitquick.ui.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewEventBinding
import com.splitquick.domain.model.Event

class EventViewHolder(
    private val binding: ViewEventBinding
) : ViewHolder(binding.root) {

    fun bind(item: Event) {
        binding.item = item
        binding.executePendingBindings()
    }

}