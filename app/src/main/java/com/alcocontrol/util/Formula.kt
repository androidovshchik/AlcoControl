@file:Suppress("LocalVariableName")

package com.alcocontrol.util

import com.alcocontrol.ALCOHOL_DENSITY
import com.alcocontrol.ELIMINATION_SPEED
import com.alcocontrol.extension.floatSumOf
import com.alcocontrol.model.Dose
import com.alcocontrol.model.Profile
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import kotlin.math.max

/**
 * Формула Видмарка для определения максимальных теоретически возможных концентраций этанола в крови
 * @see https://ru.wikipedia.org/wiki/Определение_содержания_алкоголя_в_крови
 *
 * A = V2 * К * [ALCOHOL_DENSITY]
 * с = A / (m * r) - b * t
 *
 * A - масса выпитого чистого алкоголя (г)
 * V2 - объем выпитого напитка (мл)
 * K - коэффициент крепости напитка
 * [ALCOHOL_DENSITY] - плотность спирта (кг/м3)
 * c - концентрация алкоголя в крови (%)
 * m - масса тела (кг)
 * r - коэффициент распределения Видмарка
 * b - скорость элиминации ([ELIMINATION_SPEED] г/л/ч)
 * t - время, прошедшее с момента употребления алкоголя (ч)
 * R - дефицит резорбции (при пустом желудке теряются примерно 10%, а при полном 30% выпитого алкоголя)
 * S - калибрующий коэффициент самочувствия (от 1 до 10)
 */
fun calculateBAC(profile: Profile, feeling: Float, doses: List<Dose>): Float {
    val m = profile.weight
    val r = profile.gender.r
    val b = ELIMINATION_SPEED
    val R = 0.3f
    val S = feeling
    val now = LocalDateTime.now()
    return doses.floatSumOf {
        val V2 = it.volume
        // переводим из процентов
        val K = it.drink.strength / 100
        val A = V2 * K * ALCOHOL_DENSITY
        // переводим в часы
        val t = Duration.between(it.time, now).toMinutes() / 60f
        max(0f, (A / m * r - b * t) * R * S)
    }
}