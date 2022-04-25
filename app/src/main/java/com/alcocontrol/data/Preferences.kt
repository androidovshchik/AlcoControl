package com.alcocontrol.data

import android.content.Context
import com.alcocontrol.DEFAULT_FEELING
import com.alcocontrol.model.Language
import com.alcocontrol.model.Profile
import com.alcocontrol.model.Sound
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import com.chibatching.kotpref.enumpref.nullableEnumValuePref
import com.chibatching.kotpref.gsonpref.gsonNullablePref
import com.otaliastudios.cameraview.controls.Flash
import kotlin.reflect.KProperty

class Preferences(context: Context) : KotprefModel(context) {

    override val kotprefName = "${context.packageName}_preferences"

    var language by nullableEnumValuePref<Language>(null, "language")

    var profile by gsonNullablePref<Profile>(null, "profile")

    var showGreeting by booleanPref(true, "show_greeting")

    var requestLocation by booleanPref(true, "request_location")

    var requestPush by booleanPref(true, "request_push")

    var allowNotifications by booleanPref(false, "allow_notifications")

    var requestCamera by booleanPref(true, "request_camera")

    var feeling by floatPref(DEFAULT_FEELING, "feeling")

    var soundMode by enumValuePref(Sound.ON, "sound_mode")

    var flashMode by intPref(Flash.AUTO.ordinal, "flash_mode")

    fun contains(property: KProperty<*>) = preferences.contains(getPrefKey(property))
}