package com.alcocontrol.activity

import android.os.Bundle
import com.alcocontrol.R
import kotlinx.android.synthetic.main.activity_age.*
import org.jetbrains.anko.startActivity

class AgeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age)
        tv_no.setOnClickListener {
            startActivity<LockActivity>()
        }
        tv_yes.setOnClickListener {
            startActivity<RegisterActivity>()
        }
    }
}