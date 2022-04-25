package com.alcocontrol.activity

import android.graphics.Color
import android.os.Bundle
import androidx.core.view.isVisible
import com.alcocontrol.R
import com.alcocontrol.data.Preferences
import com.alcocontrol.model.Language
import com.alcocontrol.view.DashedBox
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_language.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity() {
    
    @Inject
    lateinit var preferences: Preferences

    private var language: Language? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        tv_spinner.setOnClickListener {
            toggleSpinner()
        }
        db_russian.setOnClickListener {
            language = Language.RUSSIAN
            it as DashedBox
            notifyLanguage(it.text)
        }
        db_english.setOnClickListener {
            language = Language.ENGLISH
            it as DashedBox
            notifyLanguage(it.text)
        }
        tv_continue.setOnClickListener {
            preferences.language = language
            startActivity<AttentionActivity>()
        }
    }

    private fun toggleSpinner() {
        sv_dropdown.isVisible = !sv_dropdown.isVisible
        iv_arrow.rotation = if (sv_dropdown.isVisible) 180f else 0f
    }

    private fun notifyLanguage(name: CharSequence) {
        toggleSpinner()
        tv_spinner.text = name
        tv_continue.apply {
            backgroundTintList = null
            setTextColor(Color.WHITE)
            isEnabled = true
        }
    }
}