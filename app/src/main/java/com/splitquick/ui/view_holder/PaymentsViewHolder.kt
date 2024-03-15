package com.splitquick.ui.view_holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.R
import com.splitquick.databinding.ViewPaymentBinding
import com.splitquick.ui.model.ItemType
import com.splitquick.ui.model.Payments
import java.math.BigDecimal
import java.math.RoundingMode

class PaymentsViewHolder(
    private val context: Context,
    private val binding: ViewPaymentBinding
) : ViewHolder(binding.root) {

    fun bind(item: Payments) {
        binding.textView5.text = item.contributorName.substring(0..1)
        if (item.type == ItemType.CALCULATION) {
            binding.descriptionText.text = item.contributorName
            val amount = item.calculation.setScale(item.currency.roundingFactor, RoundingMode.HALF_UP)
            binding.amountText.text = when {
                item.calculation > BigDecimal.ZERO -> context.getString(R.string.gets_back, amount.toString(), item.currency.symbol)
                item.calculation == BigDecimal.ZERO -> context.getString(R.string.settle_up)
                else -> context.getString(R.string.owes, amount.abs().toString(), item.currency.symbol)
            }
        } else if (item.type == ItemType.HISTORY) {
            binding.descriptionText.text = item.description
            binding.amountText.text = context.getString(R.string.someone_paid, item.contributorName, "${item.contributedAmount.setScale(item.currency.roundingFactor, RoundingMode.HALF_UP)} ${item.currency.symbol}")
        }
    }

}