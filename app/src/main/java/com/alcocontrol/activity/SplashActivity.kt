package com.alcocontrol.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alcocontrol.BuildConfig
import com.alcocontrol.R
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        pb_loading.max = BuildConfig.SPLASH_DURATION / 10
        lifecycleScope.launch {
            repeat(BuildConfig.SPLASH_DURATION / 10) {
                pb_loading.progress = it
                delay(10)
            }
            if (!isFinishing) {
                navigator.navigateNext()
                finish()
            }
        }
    }
}