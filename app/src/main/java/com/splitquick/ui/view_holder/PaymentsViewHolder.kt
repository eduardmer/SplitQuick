package com.splitquick.ui.view_holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.splitquick.R
import com.splitquick.databinding.ViewPaymentBinding
import com.splitquick.ui.model.ItemType
import com.splitquick.ui.model.Payments
import kotlin.math.absoluteValue

class PaymentsViewHolder(
    private val context: Context,
    private val binding: ViewPaymentBinding
) : ViewHolder(binding.root) {

    fun bind(item: Payments) {
        binding.textView5.text = item.contributorName.substring(0..1)
        if (item.type == ItemType.CALCULATION) {
            binding.descriptionText.text = item.contributorName
            binding.amountText.text = when {
                item.calculation > 0 -> "gets back ${item.calculation}"
                item.calculation == 0.0 -> "settle up"
                else -> "owes ${item.calculation.absoluteValue}"
            }
        } else if (item.type == ItemType.HISTORY) {
            binding.descriptionText.text = item.description
            binding.amountText.text = context.getString(R.string.someone_paid, item.contributorName, item.contributedAmount)
        }
    }

}