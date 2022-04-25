package com.alcocontrol.activity

import android.os.Bundle
import androidx.activity.viewModels
import com.alcocontrol.R

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}