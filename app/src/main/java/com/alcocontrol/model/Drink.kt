package com.alcocontrol.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "drinks"
)
data class Drink(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,
    @NonNull
    @ColumnInfo(name = "name")
    var name: String,
    @NonNull
    @ColumnInfo(name = "type")
    var alcohol: Alcohol,
    @ColumnInfo(name = "strength")
    var strength: Float
)