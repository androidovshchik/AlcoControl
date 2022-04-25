package com.alcocontrol.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.alcocontrol.R
import org.jetbrains.anko.dip

class DashedLine : View {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = dip(1).toFloat()
        color = Color.parseColor("#33000000")
    }

    private var count = resources.getInteger(R.integer.division_count)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    )

    @Suppress("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun onDraw(canvas: Canvas) {
        val division = width.toFloat() / (count - 1)
        (1..count).forEach {
            var x = division * (it - 1)
            when (it) {
                1 -> x += paint.strokeWidth
                count -> x -= paint.strokeWidth
            }
            canvas.drawLine(x, 0f, x, height.toFloat(), paint)
        }
    }
}