package com.splitquick.ui.view_holder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewTitleBinding

class TitleViewHolder(private val binding: ViewTitleBinding) : ViewHolder(binding.root) {

    fun bind(stringId: Int) {
        binding.titleText.setText(stringId)
    }

}