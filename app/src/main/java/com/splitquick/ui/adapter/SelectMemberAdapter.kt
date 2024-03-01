package com.splitquick.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.splitquick.R
import com.splitquick.domain.model.Member

class SelectMemberAdapter(
    context: Context,
    private val items: MutableList<Member>
) : ArrayAdapter<Member>(context, R.layout.view_group_name) {

    companion object {
        const val HINT = 0
        const val TEXT = 1
    }

    override fun getItem(position: Int): Member {
        return items[position]
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            HINT
        else TEXT
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.view_group_name, parent, false)
        if (getItemViewType(position) == HINT)
            view.findViewById<TextView>(R.id.group_name).setText(R.string.select_member)
        else {
            val member = getItem(position)
            view.findViewById<TextView>(R.id.group_name).text = "${member.firstName} ${member.lastName}"
        }
        return view
    }

    override fun getCount(): Int {
        return items.size
    }

    fun updateData(items: List<Member>) {
        this.items.clear()
        this.items.add(Member())
        this.items.addAll(items)
        notifyDataSetChanged()
    }

}