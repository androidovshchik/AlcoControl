package com.alcocontrol.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alcocontrol.model.Drink

@Database(
    entities = [
        Drink::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun drinkDao(): DrinkDao
}