package com.alcocontrol.activity

import android.os.Bundle
import com.alcocontrol.R
import com.alcocontrol.data.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_welcome.*
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity : BaseActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        tv_continue.setOnClickListener {
            preferences.showGreeting = false
            navigator.navigateNext()
        }
    }
}