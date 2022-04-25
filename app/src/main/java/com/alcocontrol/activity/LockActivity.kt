package com.alcocontrol.activity

import android.os.Bundle
import com.alcocontrol.R
import kotlinx.android.synthetic.main.activity_lock.*

class LockActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
        tv_close.setOnClickListener {
            finishAffinity()
        }
    }
}