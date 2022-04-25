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
import kotlinx.android.synthetic.main.merge_volume.view.*

class VolumeCard : ConstraintLayout {

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
        View.inflate(context, R.layout.merge_volume, this)
        update(0)
    }

    @SuppressLint("SetTextI18n")
    fun update(volume: Int) {
        val ml = context.getString(R.string.ml)
        tv_value.text = SpannableString("$volume $ml").apply {
            setSpan(RelativeSizeSpan(2.4f), 0, toString().indexOf(' '), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}