package com.alcocontrol.model

import org.threeten.bp.LocalDateTime

class Dose(
    val drink: Drink,
    val volume: Int,
    val time: LocalDateTime = LocalDateTime.now()
)