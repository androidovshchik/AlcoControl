package com.alcocontrol.model

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.alcocontrol.R

enum class Alcohol(val type: String, val data: String, @RawRes val sound: Int, @StringRes val glass: Int) {
    COCKTAIL("Коктейли", "glass.json", R.raw.glass, R.string.other_glass),
    BEER("Пиво", "beer.json", R.raw.beer, R.string.beer_glass),
    WINE("Вино", "wine.json", R.raw.wine, R.string.wine_glass),
    HARD_DRINK("Крепкие напитки", "whiskey.json", R.raw.whiskey, R.string.whiskey_glass),
    OTHER("Разное", "glass.json", R.raw.glass, R.string.other_glass);

    companion object {

        private val map = values().associateBy(Alcohol::type)

        fun fromType(type: String?) = map[type]
    }
}