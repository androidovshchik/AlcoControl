package com.alcocontrol.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.isVisible
import com.alcocontrol.R
import com.alcocontrol.model.Alcohol
import kotlinx.android.synthetic.main.merge_kit.view.*

class AlcoholKit : ConstraintLayout {

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
        View.inflate(context, R.layout.merge_kit, this)
        attrs?.let { set ->
            context.obtainStyledAttributes(set, R.styleable.AlcoholKit).use {
                val color = it.getColor(R.styleable.AlcoholKit_custom_textColor, Color.WHITE)
                tv_beer.setTextColor(color)
                tv_wine.setTextColor(color)
                tv_glass.setTextColor(color)
                tv_whiskey.setTextColor(color)
            }
        }
    }

    fun show(alcohol: Alcohol) {
        when (alcohol) {
            Alcohol.BEER -> {
                iv_beer.isVisible = true
                tv_beer.isVisible = true
            }
            Alcohol.WINE -> {
                iv_wine.isVisible = true
                tv_wine.isVisible = true
            }
            Alcohol.HARD_DRINK -> {
                iv_whiskey.isVisible = true
                tv_whiskey.isVisible = true
            }
            else -> {
                iv_glass.isVisible = true
                tv_glass.isVisible = true
            }
        }
    }

    fun update(map: Map<Alcohol, Int>) {
        var glassCount = 0
        map.forEach { (alcohol, count) ->
            when (alcohol) {
                Alcohol.BEER -> {
                    iv_beer.isVisible = true
                    tv_beer.text = count.toString()
                    tv_beer.isVisible = true
                }
                Alcohol.WINE -> {
                    iv_wine.isVisible = true
                    tv_wine.text = count.toString()
                    tv_wine.isVisible = true
                }
                Alcohol.HARD_DRINK -> {
                    iv_whiskey.isVisible = true
                    tv_whiskey.text = count.toString()
                    tv_whiskey.isVisible = true
                }
                else -> {
                    glassCount += count
                    iv_glass.isVisible = true
                    tv_glass.text = glassCount.toString()
                    tv_glass.isVisible = true
                }
            }
        }
    }
}