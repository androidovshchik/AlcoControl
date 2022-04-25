package com.alcocontrol.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.temporal.ChronoUnit

class Plan {

    lateinit var drink: Drink

    var volume = 0

    var amount = 0

    lateinit var time: LocalDateTime
        private set

    fun setTime(time: LocalTime) {
        val now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES)
        var day = LocalDate.now()
        if (now.isBefore(time)) {
            day = day.minusDays(1)
        }
        this.time = LocalDateTime.of(day, time)
    }
}