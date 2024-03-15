package com.splitquick.ui.binding

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.splitquick.R
import com.splitquick.domain.model.Currency
import com.splitquick.domain.model.Event
import com.splitquick.domain.model.EventType
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date

@BindingAdapter("icon")
fun TextView.setInitialsAsIcon(name: String) {
    text = name.substring(0..1)
}

@BindingAdapter("expenses", "currency")
fun TextView.setTotalExpenses(value: BigDecimal, currency: Currency) {
    text = if (value == BigDecimal.ZERO)
        context.getString(R.string.no_expenses)
    else context.getString(R.string.total_expenses, "${value.setScale(currency.roundingFactor, RoundingMode.HALF_UP)} ${currency.symbol}")
}

@BindingAdapter("htmlText")
fun TextView.setHtmlText(item: Event) {
    text = when(item.action) {
        EventType.CreateGroupEvent -> HtmlCompat.fromHtml(context.getString(item.action.event, item.groupName), HtmlCompat.FROM_HTML_MODE_LEGACY)
        EventType.AddMemberEvent -> HtmlCompat.fromHtml(context.getString(item.action.event, item.memberName, item.groupName), HtmlCompat.FROM_HTML_MODE_LEGACY)
        EventType.AddExpenseEvent -> HtmlCompat.fromHtml(context.getString(item.action.event, item.memberName, item.expenseDescription, item.groupName), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

@BindingAdapter("date")
fun TextView.setDate(date: Long) {
    text = SimpleDateFormat("dd MMM yyyy HH:mm").format(Date(date))
}

@BindingAdapter("navigationOnClick")
fun MaterialToolbar.setNavigationOnClickListener(value: Boolean) {
    if (value)
        setNavigationOnClickListener {
            findNavController().navigateUp()
        }
}