package com.splitquick.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.databinding.ViewPaymentBinding
import com.splitquick.databinding.ViewTitleBinding
import com.splitquick.ui.model.ItemType
import com.splitquick.ui.model.Payments
import com.splitquick.ui.view_holder.PaymentsViewHolder
import com.splitquick.ui.view_holder.TitleViewHolder

class PaymentsHistoryAdapter(
    private val context: Context
) : ListAdapter<Payments, ViewHolder>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            ItemType.TITLE.ordinal -> {
                val binding = ViewTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TitleViewHolder(binding)
            }
            else -> {
                val binding = ViewPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PaymentsViewHolder(context, binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is TitleViewHolder)
            holder.bind(getItem(position).title)
        else if (holder is PaymentsViewHolder)
            holder.bind(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<Payments>() {
        override fun areItemsTheSame(oldItem: Payments, newItem: Payments): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Payments, newItem: Payments): Boolean {
            return oldItem == newItem
        }
    }

}