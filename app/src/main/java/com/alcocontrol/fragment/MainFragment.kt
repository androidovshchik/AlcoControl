package com.alcocontrol.fragment

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.alcocontrol.R
import com.alcocontrol.activity.CameraActivity
import com.alcocontrol.activity.MainViewModel
import com.alcocontrol.model.Dose
import com.alcocontrol.ppmLevels
import com.warkiz.widget.IndicatorSeekBar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jetbrains.anko.intentFor
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.min

class MainFragment : Fragment(), View.OnClickListener {

    private val viewModel: MainViewModel by activityViewModels()

    private val navController by lazy { findNavController() }

    private val menuDialog by lazy { MenuDialog.newInstance() }

    private val intoxDialog by lazy { IntoxDialog.newInstance() }

    private var signalPlayer: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_close.setOnClickListener(this)
        iv_camera.setOnClickListener(this)
        iv_search.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        iv_info.setOnClickListener(this)
        tv_next.setOnClickListener(this)
        viewModel.plan.observe(viewLifecycleOwner, {
            updateCharacter()
            al_all.show(it.drink.alcohol)
            updateButton()
        })
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.doses.collect {
                val ppm = viewModel.commonPpm
                ic_intox.update(ppm)
                ic_time.update(viewModel.commonTime)
                ic_volume.update(viewModel.commonVolume)
                al_all.update(viewModel.alcoholKit)
                changeProgress(ppm)
                updateButton()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                val ppm = viewModel.commonPpm
                ic_intox.update(ppm)
                ic_time.update(viewModel.commonTime)
                changeProgress(ppm)
                updateButton()
                delay(TimeUnit.MINUTES.toMillis(1))
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab_close -> {
                fab_close.isVisible = false
                iv_bubble.isVisible = false
                tv_bubble.isVisible = false
            }
            R.id.iv_camera -> {
                val context = requireContext()
                startActivity(context.intentFor<CameraActivity>(
                    CameraActivity.EXTRA_PPM to viewModel.commonPpm,
                    CameraActivity.EXTRA_TIME to viewModel.commonTime,
                    CameraActivity.EXTRA_VOLUME to viewModel.commonVolume,
                    CameraActivity.EXTRA_MAP to viewModel.alcoholKit
                ))
            }
            R.id.iv_search -> {
                navController.navigate(R.id.planFragment)
            }
            R.id.iv_menu -> {
                if (!menuDialog.isAdded) {
                    menuDialog.show(childFragmentManager, menuDialog.javaClass.simpleName)
                }
            }
            R.id.iv_info -> {
                if (!intoxDialog.isAdded) {
                    intoxDialog.show(childFragmentManager, intoxDialog.javaClass.simpleName)
                }
            }
            R.id.tv_next -> {
                viewModel.plan.value?.let {
                    playSignal(it.drink.alcohol.sound)
                    viewModel.doses.tryEmit(Dose(it.drink, it.volume))
                    updateCharacter()
                }
            }
        }
    }

    private fun updateCharacter() {
        val context = context ?: return
        with(viewModel) {
            val alcohol = plan.value?.drink?.alcohol ?: return
            val charKit = alcoholChars.value[alcohol].takeIf { !it.isNullOrEmpty() } ?: return
            val quantity = alcoholKit[alcohol] ?: 0
            val character = charKit[min(quantity, charKit.lastIndex)].random()
            fab_close.isVisible = true
            iv_bubble.isVisible = true
            tv_bubble.isVisible = true
            with(resources) {
                val id = getIdentifier(character.array, "array", context.packageName)
                tv_bubble.text = getStringArray(id).random()
            }
            iv_character.load(Uri.parse("file:///android_asset/${character.image}"))
        }
    }

    private fun changeProgress(ppm: Float) {
        val tickCount = sb_intox.tickCount
        val mProgressArr = IndicatorSeekBar::class.java.getDeclaredField("mProgressArr")
        mProgressArr.isAccessible = true
        val progressValues = mProgressArr.get(sb_intox) as FloatArray
        val index = (0 until tickCount).withIndex()
            .minByOrNull { abs(ppm - calculatePpmValue(it.value, tickCount)) }!!
            .index
        sb_intox.setProgress(progressValues[index])
    }

    private fun updateButton() {
        val context = context ?: return
        with(viewModel) {
            val alcohol = plan.value?.drink?.alcohol ?: return
            val ppm = commonPpm
            val quantity = alcoholKit[alcohol] ?: 0
            tv_next.apply {
                backgroundTintList = ContextCompat.getColorStateList(context, when {
                    ppm < 3f -> R.color.colorGreen
                    ppm < 5f -> R.color.colorRed
                    else -> R.color.colorRed50
                })
                text = getString(alcohol.glass, quantity + 1)
                setTextColor(Color.WHITE)
                isEnabled = ppm < 5f
            }
        }
    }

    private fun playSignal(@RawRes id: Int) {
        releasePlayer()
        val context = context ?: return
        try {
            signalPlayer = MediaPlayer.create(context, id)
            signalPlayer?.start()
        } catch (e: Throwable) {
        }
    }

    private fun releasePlayer() {
        try {
            signalPlayer?.stop()
            signalPlayer?.reset()
            signalPlayer?.release()
        } catch (e: Throwable) {
        }
    }

    override fun onStop() {
        releasePlayer()
        super.onStop()
    }

    companion object : Instance<MainFragment>(MainFragment::class.java) {

        private fun calculatePpmValue(i: Int, tickCount: Int): Float {
            val perTickCount = (tickCount - 1) / (ppmLevels.size - 1)
            for (j in 1..ppmLevels.lastIndex) {
                val step = (ppmLevels[j] - ppmLevels[j - 1]) / perTickCount
                for (k in 0..perTickCount) {
                    if (i == (j - 1) * perTickCount + k) {
                        return ppmLevels[j - 1] + step * k
                    }
                }
            }
            throw RuntimeException()
        }
    }
}