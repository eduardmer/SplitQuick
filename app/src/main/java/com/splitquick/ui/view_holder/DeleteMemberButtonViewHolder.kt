package com.splitquick.ui.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewDeleteMemberButtonBinding

class DeleteMemberButtonViewHolder(
    private val binding: ViewDeleteMemberButtonBinding
) : ViewHolder(binding.root) {

    fun bind(isEnabled: Boolean, onClick: () -> Unit) {
        binding.deleteButton.isEnabled = isEnabled
        binding.deleteButton.setOnClickListener {
            onClick()
        }
    }

}