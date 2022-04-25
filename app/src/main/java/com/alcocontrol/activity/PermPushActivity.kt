package com.alcocontrol.activity

import android.os.Bundle
import com.alcocontrol.R
import com.alcocontrol.data.Preferences
import com.chibatching.kotpref.bulk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_perm_push.*
import javax.inject.Inject

@AndroidEntryPoint
class PermPushActivity : BaseActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perm_push)
        tv_skip.setOnClickListener {
            preferences.requestPush = false
            navigator.navigateNext()
        }
        tv_allow.setOnClickListener {
            preferences.bulk {
                requestPush = false
                allowNotifications = true
            }
            navigator.navigateNext()
        }
    }
}