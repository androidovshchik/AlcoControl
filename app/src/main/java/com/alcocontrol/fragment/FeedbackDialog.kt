package com.alcocontrol.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.alcocontrol.R
import kotlinx.android.synthetic.main.fragment_feedback.*
import kotlinx.android.synthetic.main.include_close.*
import org.jetbrains.anko.textColorResource

class FeedbackDialog : SheetDialog() {

    private var textWatcher: TextWatcher? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = requireArguments()
        tv_title.text = getString(args.getInt(TITLE))
        tv_title.textSize = args.getFloat(TITLE_SIZE)
        fab_close.setOnClickListener {
            dismiss()
        }
        il_mail.addTextChangedListener {
            checkInput()
        }
        tv_desc.text = getString(args.getInt(CAPTION))
        et_text.filters = arrayOf(InputFilter.AllCaps())
        textWatcher = et_text.addTextChangedListener(afterTextChanged = {
            checkInput()
        })
        tv_send.setOnClickListener {
            dismiss()
        }
    }

    private fun checkInput() {
        try {
            val mail = il_mail.text.toString()
            require(mail.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                il_mail.showError()
                0
            }
            il_mail.hideError()
            val text = et_text.text.toString()
            require(text.isNotBlank()) {
                tv_text_error.isVisible = true
                0
            }
            tv_text_error.isVisible = false
            tv_send.apply {
                backgroundTintList = null
                setTextColor(Color.WHITE)
                isEnabled = true
            }
            return
        } catch (e: Throwable) {
        }
        tv_send.apply {
            if (isEnabled) {
                backgroundTintList = ContextCompat.getColorStateList(context, R.color.colorHighlight)
                textColorResource = R.color.colorGray50
                isEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        et_text.removeTextChangedListener(textWatcher)
        super.onDestroyView()
    }

    companion object : Instance<FeedbackDialog>(FeedbackDialog::class.java) {

        const val TITLE = "title"

        const val TITLE_SIZE = "size"

        const val CAPTION = "caption"
    }
}