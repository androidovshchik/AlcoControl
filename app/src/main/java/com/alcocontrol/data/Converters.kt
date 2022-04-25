package com.alcocontrol.data

import androidx.room.TypeConverter
import com.alcocontrol.model.Alcohol

class Converters {

    @TypeConverter
    fun toAlcohol(value: String?): Alcohol? {
        return Alcohol.fromType(value)
    }

    @TypeConverter
    fun fromAlcohol(date: Alcohol?): String? {
        return date?.type
    }
}