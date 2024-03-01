package com.splitquick.data.database

import androidx.room.TypeConverter
import com.splitquick.domain.model.EventType

class TypeConverter {

    @TypeConverter
    fun enumToInt(value: EventType): Int = value.ordinal

    @TypeConverter
    fun intToEnum(nr: Int): EventType = enumValues<EventType>()[nr]

}