package com.alcocontrol.data

import com.alcocontrol.model.Alcohol
import com.alcocontrol.model.Drink
import javax.inject.Inject

class DrinkRepository @Inject constructor(
    private val drinkDao: DrinkDao
) {

    val allDrinks: Map<Alcohol, List<Drink>>
        get() {
            return drinkDao.selectAll().groupBy { it.alcohol }
        }
}