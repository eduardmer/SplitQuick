package com.splitquick.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewAddGroupBinding
import com.splitquick.databinding.ViewGroupBinding
import com.splitquick.domain.model.Group
import com.splitquick.ui.view_holder.GroupViewHolder
import com.splitquick.ui.view_holder.AddGroupViewHolder

class GroupsAdapter(
    private val onButtonClick: () -> Unit,
    private val onGroupClicked: (Long) -> Unit
) : ListAdapter<Group, ViewHolder>(DiffCallBack()) {

    companion object {
        const val BUTTON = 0
        const val GROUP = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            BUTTON
        else GROUP
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == BUTTON)
            AddGroupViewHolder(ViewAddGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else
            GroupViewHolder(ViewGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is AddGroupViewHolder)
            holder.bind(onButtonClick)
        else if (holder is GroupViewHolder)
            holder.bind(getItem(position), onGroupClicked)
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    class DiffCallBack : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }
    }

}