package com.alcocontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alcocontrol.DEFAULT_FEELING
import com.alcocontrol.MIN_FEELING
import com.alcocontrol.R
import com.alcocontrol.activity.MainViewModel
import com.alcocontrol.data.Preferences
import com.alcocontrol.extension.cutTrailingZeros
import com.alcocontrol.extension.navigateOnce
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feeling.*
import org.jetbrains.anko.textColorResource
import javax.inject.Inject

@AndroidEntryPoint
class FeelingFragment : Fragment(), OnSeekChangeListener {

    @Inject
    lateinit var preferences: Preferences

    private val viewModel: MainViewModel by activityViewModels()

    private val navController by lazy { findNavController() }

    private val careDialog by lazy { CareDialog.newInstance() }

    private var feeling = DEFAULT_FEELING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feeling = preferences.feeling
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feeling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_continue.setOnClickListener {
            when {
                feeling <= MIN_FEELING -> {
                    if (!careDialog.isAdded) {
                        careDialog.show(childFragmentManager, careDialog.javaClass.simpleName)
                    }
                }
                viewModel.plan.value != null -> {
                    navController.popBackStack(R.id.feelingFragment, true)
                    navController.navigateOnce(R.id.mainFragment)
                }
                else -> {
                    navController.popBackStack(R.id.feelingFragment, true)
                    navController.navigateOnce(R.id.planFragment)
                }
            }
        }
        sb_rating.setProgress((feeling - 1) / 0.25f * sb_rating.max / (sb_rating.tickCount - 1))
        sb_rating.onSeekChangeListener = this
        notifyFeeling()
    }

    override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {
    }

    override fun onSeeking(seekParams: SeekParams) {
        feeling = 1 + seekParams.thumbPosition * 0.25f
        notifyFeeling()
    }

    override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {
        preferences.feeling = feeling
    }

    private fun notifyFeeling() {
        tv_rating.textColorResource = if (feeling > MIN_FEELING) R.color.colorGreen else R.color.colorRed
        tv_rating.text = feeling.toString().cutTrailingZeros()
    }

    override fun onDestroyView() {
        sb_rating.onSeekChangeListener = null
        super.onDestroyView()
    }

    companion object : Instance<FeelingFragment>(FeelingFragment::class.java)
}