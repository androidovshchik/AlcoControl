package com.alcocontrol.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController

fun NavController.navigateOnce(@IdRes resId: Int, args: Bundle? = null) {
    if (currentDestination?.id != resId) {
        try {
            getBackStackEntry(resId)
            popBackStack(resId, false)
            return
        } catch (ignored: Throwable) {
        }
        navigate(resId, args)
    }
}