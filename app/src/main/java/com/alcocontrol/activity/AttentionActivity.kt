package com.alcocontrol.activity

import android.os.Bundle
import com.alcocontrol.R
import kotlinx.android.synthetic.main.activity_attention.*
import org.jetbrains.anko.startActivity

class AttentionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attention)
        tv_ok.setOnClickListener {
            startActivity<AgeActivity>()
        }
    }
}