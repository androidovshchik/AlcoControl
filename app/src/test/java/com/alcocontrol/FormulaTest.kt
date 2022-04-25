package com.alcocontrol

import com.alcocontrol.model.*
import com.alcocontrol.util.calculateBAC
import org.junit.Test
import org.threeten.bp.LocalDateTime

class FormulaTest {

    @Test
    fun bac_test() {
        val profile = Profile().apply {
            weight = 95f
            gender = Gender.MALE
        }
        val vodka = Drink(0, "", Alcohol.HARD_DRINK, 40f)
        val now = LocalDateTime.now()
        val bac = calculateBAC(profile, 9f, listOf(
            Dose(vodka, 50, now.minusMinutes(20))
        ))
        println(bac)
    }
}