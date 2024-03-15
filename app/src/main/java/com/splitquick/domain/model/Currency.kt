package com.splitquick.domain.model

import com.splitquick.R

enum class Currency(val fullName: String, val symbol: String, val roundingFactor: Int, val icon: Int) {
    EUR("Euro", "€", 2, R.drawable.ic_euro_symbol),
    USD("US Dollar", "US$", 1, R.drawable.ic_dollar_symbol),
    ALL("Albanian Lek", "ALL", 0, R.drawable.ic_lek_symbol);

    override fun toString(): String {
        return fullName
    }
}