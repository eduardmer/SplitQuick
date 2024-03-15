package com.splitquick.ui.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewAddMemberButtonBinding

class AddMemberButtonViewHolder(
    private val binding: ViewAddMemberButtonBinding
    ) : ViewHolder(binding.root) {

    fun bind(onClick: () -> Unit) {
        binding.filledButton.setOnClickListener {
            onClick()
        }
    }

}