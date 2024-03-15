package com.splitquick.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.splitquick.R
import com.splitquick.domain.model.Currency

class CurrencyAdapter(
    context: Context,
    private val items: List<Currency>
) : ArrayAdapter<Currency>(context, R.layout.view_currency) {

    override fun getItem(position: Int): Currency {
        return items[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.view_currency, parent, false)
        val item = getItem(position)
        view.findViewById<TextView>(R.id.currency_name).text = "${item.fullName} (${item.symbol})"
        view.findViewById<TextView>(R.id.currency_symbol).text = item.name
        return view
    }

    override fun getCount(): Int {
        return items.size
    }

}