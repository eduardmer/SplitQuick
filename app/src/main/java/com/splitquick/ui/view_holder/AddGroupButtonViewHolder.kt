package com.splitquick.ui.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewAddGroupButtonBinding

class AddGroupButtonViewHolder(
    private val binding: ViewAddGroupButtonBinding
) : ViewHolder(binding.root) {

    fun bind(onClick: () -> Unit) {
        binding.addGroup.setOnClickListener {
            onClick()
        }
    }

}