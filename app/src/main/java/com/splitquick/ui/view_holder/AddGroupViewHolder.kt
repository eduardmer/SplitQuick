package com.splitquick.ui.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewAddGroupBinding

class AddGroupViewHolder(
    private val binding: ViewAddGroupBinding
) : ViewHolder(binding.root) {

    fun bind(onClick: () -> Unit) {
        binding.addGroup.setOnClickListener {
            onClick()
        }
    }

}