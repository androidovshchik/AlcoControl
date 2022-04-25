package com.alcocontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alcocontrol.R
import com.alcocontrol.data.Preferences
import com.alcocontrol.model.Sound
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.include_close.*
import javax.inject.Inject

@AndroidEntryPoint
class NotificationDialog : SheetDialog(), View.OnClickListener {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_close.setOnClickListener(this)
        db_notification_on.setOnClickListener(this)
        db_notification_off.setOnClickListener(this)
        db_sound_on.setOnClickListener(this)
        db_sound_vibration.setOnClickListener(this)
        db_sound_off.setOnClickListener(this)
        notifyNotification(preferences.allowNotifications)
        notifySound(preferences.soundMode)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab_close -> {
                dismiss()
            }
            R.id.db_notification_on -> {
                preferences.allowNotifications = true
                notifyNotification(true)
            }
            R.id.db_notification_off -> {
                preferences.allowNotifications = false
                notifyNotification(false)
            }
            R.id.db_sound_on -> {
                val mode = Sound.ON
                preferences.soundMode = mode
                notifySound(mode)
            }
            R.id.db_sound_vibration -> {
                val mode = Sound.VIBRATION
                preferences.soundMode = mode
                notifySound(mode)
            }
            R.id.db_sound_off -> {
                val mode = Sound.OFF
                preferences.soundMode = mode
                notifySound(mode)
            }
        }
    }

    private fun notifyNotification(allow: Boolean) {
        db_notification_on.isChecked = allow
        db_notification_off.isChecked = !allow
    }

    private fun notifySound(mode: Sound) {
        db_sound_on.isChecked = mode == Sound.ON
        db_sound_vibration.isChecked = mode == Sound.VIBRATION
        db_sound_off.isChecked = mode == Sound.OFF
    }

    companion object : Instance<NotificationDialog>(NotificationDialog::class.java)
}