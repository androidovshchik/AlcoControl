package com.alcocontrol.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.alcocontrol.R
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.merge_edit.view.*

class InputLayout : ConstraintLayout {

    var text: CharSequence
        get() = et_input.text
        set(value) {
            et_input.setText(value)
            et_input.setSelection(value.length)
        }

    private var watcher: TextWatcher? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    @Suppress("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.merge_edit, this)
        isClickable = true
        isFocusable = true
        et_input.filters = arrayOf(InputFilter.AllCaps())
        attrs?.let { set ->
            context.obtainStyledAttributes(set, R.styleable.InputLayout).use {
                et_input.apply {
                    inputType = it.getInt(R.styleable.InputLayout_custom_type, InputType.TYPE_CLASS_TEXT)
                    val defColor = ContextCompat.getColor(context, R.color.colorGray30)
                    val color = it.getColor(R.styleable.InputLayout_custom_textColor, defColor)
                    setTextColor(color)
                    hint = it.getString(R.styleable.InputLayout_custom_hint)
                    setHintTextColor(color)
                    imeOptions = it.getInt(R.styleable.InputLayout_custom_ime, EditorInfo.IME_NULL)
                }
                tv_suffix.text = it.getString(R.styleable.InputLayout_custom_suffix)
                tv_error.text = it.getString(R.styleable.InputLayout_custom_error)
                if (it.getBoolean(R.styleable.InputLayout_custom_goneError, false)) {
                    tv_error.isVisible = false
                }
            }
        }
    }

    fun addTextMask(format: String) {
        MaskedTextChangedListener.installOn(et_input, format)
    }

    fun addTextChangedListener(body: (text: Editable?) -> Unit) {
        watcher = et_input.addTextChangedListener(afterTextChanged = body)
    }

    fun setSuffix(suffix: Any) {
        tv_suffix.text = suffix.toString()
    }

    fun showError() {
        tv_error.visibility = VISIBLE
    }

    fun showError(@StringRes id: Int) {
        tv_error.setText(id)
        tv_error.visibility = VISIBLE
    }

    fun hideError() {
        tv_error.visibility = INVISIBLE
    }

    override fun onDetachedFromWindow() {
        et_input.removeTextChangedListener(watcher)
        super.onDetachedFromWindow()
    }
}