package com.splitquick.ui.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewDeleteButtonBinding

class DeleteMemberViewHolder(
    private val binding: ViewDeleteButtonBinding
) : ViewHolder(binding.root) {

    fun bind(isEnabled: Boolean, onClick: () -> Unit) {
        binding.deleteButton.isEnabled = isEnabled
        binding.deleteButton.setOnClickListener {
            onClick()
        }
    }

}