package com.alcocontrol.extension

fun String.cutTrailingZeros(): String {
    return replace("\\.0*$".toRegex(), "")
}