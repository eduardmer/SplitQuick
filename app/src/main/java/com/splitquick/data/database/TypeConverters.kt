package com.splitquick.data.database

import androidx.room.TypeConverter
import com.splitquick.domain.model.EventType
import java.math.BigDecimal

class TypeConverter {

    @TypeConverter
    fun enumToInt(value: EventType): Int = value.ordinal

    @TypeConverter
    fun intToEnum(nr: Int): EventType = enumValues<EventType>()[nr]

    @TypeConverter
    fun bigDecimalToString(value: BigDecimal): String = value.toString()

    @TypeConverter
    fun stringToBigDecimal(value: String?): BigDecimal {
        return if (value == null)
            BigDecimal.ZERO
        else BigDecimal(value)
    }

}