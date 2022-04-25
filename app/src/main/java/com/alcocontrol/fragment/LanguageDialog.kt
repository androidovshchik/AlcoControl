package com.alcocontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alcocontrol.R
import com.alcocontrol.data.Preferences
import com.alcocontrol.model.Language
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_language.*
import kotlinx.android.synthetic.main.include_close.*
import javax.inject.Inject

@AndroidEntryPoint
class LanguageDialog : SheetDialog() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_close.setOnClickListener {
            dismiss()
        }
        db_russian.setOnClickListener {
            val language = Language.RUSSIAN
            preferences.language = language
            notifyLanguage(language)
        }
        db_english.setOnClickListener {
            val language = Language.ENGLISH
            preferences.language = language
            notifyLanguage(language)
        }
        notifyLanguage(preferences.language!!)
    }

    private fun notifyLanguage(language: Language) {
        db_russian.isChecked = language == Language.RUSSIAN
        db_english.isChecked = language == Language.ENGLISH
    }

    companion object : Instance<LanguageDialog>(LanguageDialog::class.java)
}