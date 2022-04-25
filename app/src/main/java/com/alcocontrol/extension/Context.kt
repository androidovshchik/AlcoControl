@file:Suppress("unused")

package com.alcocontrol.extension

import android.content.Context
import android.content.pm.PackageManager

fun Context.areGranted(vararg permissions: String): Boolean {
    return permissions.all { isGranted(it) }
}

fun Context.isGranted(permission: String): Boolean {
    return checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}