package com.alcocontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import com.alcocontrol.MAX_LEVEL
import com.alcocontrol.MIN_LEVEL
import com.alcocontrol.R
import com.alcocontrol.ppmLevels
import kotlinx.android.synthetic.main.fragment_intox.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class IntoxDialog : DocDialog() {

    private var initLevel = 0

    private var navLevel = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_intox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ppm = viewModel.commonPpm
        initLevel = ppmLevels.indexOfFirst { it > ppm }
        navLevel = initLevel
        fab_close.setOnClickListener {
            dismiss()
        }
        iv_left.setOnClickListener {
            navLevel = max(MIN_LEVEL, --navLevel)
            notifyLevel()
        }
        iv_right.setOnClickListener {
            navLevel = min(MAX_LEVEL, ++navLevel)
            notifyLevel()
        }
        notifyLevel()
    }

    private fun notifyLevel() {
        tv_title.text = if (initLevel == navLevel) {
            getString(R.string.intox_my_level, navLevel)
        } else {
            getString(R.string.intox_level, navLevel)
        }
        iv_left.isInvisible = navLevel <= MIN_LEVEL
        tv_progress.text = getString(R.string.level_progress, navLevel, MAX_LEVEL)
        iv_right.isInvisible = navLevel >= MAX_LEVEL
        viewLifecycleOwner.lifecycleScope.coroutineContext[Job]?.cancelChildren()
        viewLifecycleOwner.lifecycleScope.launch {
            tv_text.text = HtmlCompat.fromHtml(
                viewModel.readText("level$navLevel.html"),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

    companion object : Instance<IntoxDialog>(IntoxDialog::class.java)
}