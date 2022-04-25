package com.alcocontrol.extension

inline fun <T> Iterable<T>.floatSumOf(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) {
        sum += selector(element)
    }
    return sum
}