package com.splitquick.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewAddMemberBinding
import com.splitquick.databinding.ViewAddMemberButtonBinding
import com.splitquick.databinding.ViewDeleteButtonBinding
import com.splitquick.domain.model.Member
import com.splitquick.ui.view_holder.AddMemberViewHolder
import com.splitquick.ui.view_holder.DeleteMemberViewHolder
import com.splitquick.ui.view_holder.MemberViewHolder

class AddMemberAdapter(
    private val items: MutableList<Member>
) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        private const val ADD_BUTTON = 0
        private const val DELETE_BUTTON = 1
        private const val TEXT_FIELD = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            itemCount - 1 -> ADD_BUTTON
            itemCount - 2 -> DELETE_BUTTON
            else -> TEXT_FIELD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            ADD_BUTTON -> AddMemberViewHolder(ViewAddMemberButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            DELETE_BUTTON -> DeleteMemberViewHolder(ViewDeleteButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> MemberViewHolder(ViewAddMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is DeleteMemberViewHolder -> holder.bind(items.size > 2) {
                deleteItem()
            }
            is AddMemberViewHolder -> holder.bind {
                addItem()
            }
            is MemberViewHolder -> holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size + 2
    }

    private fun addItem() {
        items.add(itemCount - 2, Member())
        notifyItemInserted(itemCount - 2)
    }

    private fun deleteItem() {
        items.removeLast()
        notifyItemRemoved(items.size)
        notifyItemChanged(itemCount - 2)
    }

}