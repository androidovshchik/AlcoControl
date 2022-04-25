package com.alcocontrol.data

import androidx.room.Dao
import androidx.room.Query
import com.alcocontrol.model.Drink

@Dao
abstract class DrinkDao {

    @Query(
        """
        SELECT * FROM drinks
        ORDER BY name ASC
    """
    )
    abstract fun selectAll(): List<Drink>
}