package com.alcocontrol.model

/**
 * @property r - коэффициент распределения Видмарка
 */
enum class Gender(val r: Float) {
    MALE(0.7f), FEMALE(0.6f)
}