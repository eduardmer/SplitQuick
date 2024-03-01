package com.splitquick.ui.view_holder

import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewAddMemberBinding
import com.splitquick.domain.model.Member

class MemberViewHolder(private val binding: ViewAddMemberBinding) : ViewHolder(binding.root) {

    fun bind(item: Member) {
        binding.firstNameText.doAfterTextChanged {
            item.firstName = it?.toString()?.trim() ?: ""
        }
        binding.lastNameText.doAfterTextChanged {
            item.lastName = it?.toString()?.trim() ?: ""
        }
    }

}