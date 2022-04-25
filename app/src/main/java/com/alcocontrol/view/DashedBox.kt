package com.alcocontrol.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import com.alcocontrol.R
import kotlinx.android.synthetic.main.merge_dash.view.*

class DashedBox : ConstraintLayout {

    var isChecked = false
        set(value) {
            field = value
            setBackgroundResource(if (value) R.color.colorHighlight else 0)
        }

    var text: CharSequence
        get() = tv_text.text
        set(value) {
            tv_text.text = value
        }

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
        View.inflate(context, R.layout.merge_dash, this)
        isClickable = true
        isFocusable = true
        attrs?.let { set ->
            context.obtainStyledAttributes(set, R.styleable.DashedBox).use {
                tv_text.apply {
                    tv_text.text = it.getString(R.styleable.DashedBox_custom_text)
                    tv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.getDimension(R.styleable.DashedBox_custom_size, textSize))
                }
                iv_icon.setImageDrawable(it.getDrawable(R.styleable.DashedBox_custom_icon))
                v_divider.isVisible = it.getBoolean(R.styleable.DashedBox_custom_dashed, true)
            }
        }
    }

    fun rotateIcon(angle: Float) {
        iv_icon.rotation = angle
    }
}