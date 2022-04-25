package com.alcocontrol.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.alcocontrol.R
import com.alcocontrol.data.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_perm_location.*
import javax.inject.Inject

@AndroidEntryPoint
class PermLocationActivity : BaseActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perm_location)
        tv_skip.setOnClickListener {
            preferences.requestLocation = false
            navigator.navigateNext()
        }
        val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            preferences.requestLocation = false
            navigator.navigateNext()
        }
        tv_allow.setOnClickListener {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}