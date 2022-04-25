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
import kotlinx.android.synthetic.main.merge_time.view.*
import java.util.concurrent.TimeUnit

class TimeCard : ConstraintLayout {

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
        View.inflate(context, R.layout.merge_time, this)
        update(0L)
    }

    @SuppressLint("SetTextI18n")
    fun update(seconds: Long) {
        val hours = TimeUnit.SECONDS.toHours(seconds)
        var minutes = TimeUnit.SECONDS.toMinutes(seconds)
        val m = context.getString(R.string.m)
        if (hours > 0) {
            minutes -= hours * 60
            val h = context.getString(R.string.h)
            tv_value.text = SpannableString("$hours $h $minutes $m").apply {
                val text = toString()
                val space1 = text.indexOf(' ')
                setSpan(RelativeSizeSpan(2.4f), 0, space1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val space2 = text.indexOf(' ', space1 + 1)
                val space3 = text.indexOf(' ', space2 + 1)
                setSpan(RelativeSizeSpan(2.4f), space2 + 1, space3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        } else {
            tv_value.text = SpannableString("$minutes $m").apply {
                setSpan(RelativeSizeSpan(2.4f), 0, toString().indexOf(' '), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}