package com.splitquick.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.splitquick.R
import com.splitquick.domain.model.Group

class SelectGroupAdapter(context: Context) : ArrayAdapter<Group>(context, R.layout.view_group_name) {

    private val items = mutableListOf<Group>()
    private val filteredItems = mutableListOf<Group>()

    override fun getItem(position: Int): Group {
        return filteredItems[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.view_group_name, parent, false)
        view.findViewById<TextView>(R.id.group_name).text = getItem(position).name
        return view
    }

    override fun getCount(): Int {
        return filteredItems.size
    }

    fun updateData(items: List<Group>) {
        this.items.clear()
        this.filteredItems.clear()
        this.items.addAll(items)
        this.filteredItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val results = FilterResults()
                val suggestions = mutableListOf<Group>()
                if (p0.isNullOrEmpty())
                    suggestions.addAll(items)
                else {
                    for (item in items) {
                        if (item.name.contains(p0, true))
                            suggestions.add(item)
                    }
                }
                results.values = suggestions
                results.count = suggestions.size
                return results
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredItems.clear()
                for (result in p1?.values as List<*>) {
                    if (result is Group)
                        filteredItems.add(result)
                }
                notifyDataSetChanged()
            }

        }
    }

}