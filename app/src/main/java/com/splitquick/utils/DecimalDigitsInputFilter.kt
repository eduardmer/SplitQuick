package com.splitquick.utils

import android.text.InputFilter
import android.text.Spanned
import com.splitquick.domain.model.Currency
import java.util.regex.Pattern

class DecimalDigitsInputFilter(currency: Currency) : InputFilter {

    private val mPattern = when(currency) {
        Currency.EUR -> Pattern.compile("[0-9]*+((\\.[0-9]?)?)||(\\.)?")
        Currency.USD -> Pattern.compile("[0-9]*+((\\.[0-9]{0})?)||(\\.)?")
        Currency.ALL -> Pattern.compile("[0-9]*+((\\.[0-9])?)||(\\.)?")
    }

    override fun filter(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Spanned?,
        p4: Int,
        p5: Int
    ): CharSequence? {
        val matcher = mPattern.matcher(p3)
        if (!matcher.matches())
            return ""
        return null
    }

}