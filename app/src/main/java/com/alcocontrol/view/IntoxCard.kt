package com.alcocontrol.view

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.alcocontrol.R
import kotlinx.android.synthetic.main.merge_intox.view.*

class IntoxCard : ConstraintLayout {

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
        View.inflate(context, R.layout.merge_intox, this)
        update(0f)
    }

    @SuppressLint("SetTextI18n")
    fun update(ppm: Float) {
        tv_value.text = SpannableString("${String.format("%.1f", ppm)} ${0x2030.toChar()}").apply {
            setSpan(RelativeSizeSpan(1.6f), 0, toString().indexOf(' '), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}