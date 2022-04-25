package com.alcocontrol.activity

import android.os.Bundle
import com.alcocontrol.fragment.ProfileFragment

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, ProfileFragment.newInstance())
            .commit()
    }
}