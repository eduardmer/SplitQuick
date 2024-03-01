package com.splitquick.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.R
import com.splitquick.databinding.ViewEventBinding
import com.splitquick.databinding.ViewTitleBinding
import com.splitquick.domain.model.Event
import com.splitquick.ui.view_holder.EventViewHolder
import com.splitquick.ui.view_holder.TitleViewHolder

class EventsAdapter(
    private val context: Context
) : ListAdapter<Event, ViewHolder>(DiffCallBack()) {

    companion object {
        const val TITLE = 0
        const val EVENT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            TITLE
        else EVENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TITLE) {
            val binding = ViewTitleBinding.inflate(LayoutInflater.from(context), parent, false)
            TitleViewHolder(binding)
        } else {
            val binding = ViewEventBinding.inflate(LayoutInflater.from(context), parent, false)
            EventViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is TitleViewHolder)
            holder.bind(R.string.activity)
        else if (holder is EventViewHolder)
            holder.bind(getItem(position - 1))
    }

    override fun getItemCount(): Int {
        return currentList.size + 1
    }

    class DiffCallBack : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

}