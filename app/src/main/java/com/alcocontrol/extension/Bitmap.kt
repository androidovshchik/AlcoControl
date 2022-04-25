@file:Suppress("unused")

package com.alcocontrol.extension

import android.graphics.Bitmap

inline fun <T> Bitmap.use(block: (Bitmap) -> T): T {
    try {
        return block(this)
    } finally {
        try {
            recycle()
        } catch (e: Throwable) {
        }
    }
}