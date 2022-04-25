package com.alcocontrol.activity

import com.alcocontrol.data.Preferences
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import javax.inject.Inject

class Navigator @Inject constructor(
    private val preferences: Preferences
) {

    fun navigateNext() {
        with(preferences) {
            val intent = when {
                profile == null -> context.intentFor<LanguageActivity>()
                showGreeting -> context.intentFor<WelcomeActivity>()
                requestLocation -> context.intentFor<PermLocationActivity>()
                requestPush -> context.intentFor<PermPushActivity>()
                requestCamera -> context.intentFor<PermCameraActivity>()
                else -> context.intentFor<MainActivity>()
            }
            context.startActivity(intent.newTask())
        }
    }
}