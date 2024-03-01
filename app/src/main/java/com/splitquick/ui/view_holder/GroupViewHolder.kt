package com.splitquick.ui.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewGroupBinding
import com.splitquick.domain.model.Group

class GroupViewHolder(private val binding: ViewGroupBinding) : ViewHolder(binding.root) {

    fun bind(item: Group, onGroupClicked: (Long) -> Unit) {
        binding.item = item
        binding.root.setOnClickListener {
            onGroupClicked(item.id)
        }
        binding.executePendingBindings()
    }

}