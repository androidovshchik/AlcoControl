package com.alcocontrol

import org.threeten.bp.format.DateTimeFormatter

const val MIN_FEELING = 7f

const val DEFAULT_FEELING = 7f

const val ADULT_AGE = 18

const val ALCOHOL_DENSITY = 0.789f

const val ELIMINATION_SPEED = 0.15f

const val MIN_LEVEL = 1

const val MAX_LEVEL = 9

val ppmLevels = floatArrayOf(0f, 0.2f, 0.3f, 0.6f, 1f, 2f, 3f, 4f, 5f, 1000f)

val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

val datetimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss.SSS")