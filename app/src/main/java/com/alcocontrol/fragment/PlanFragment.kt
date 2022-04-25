package com.alcocontrol.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alcocontrol.R
import com.alcocontrol.activity.MainViewModel
import com.alcocontrol.extension.navigateOnce
import com.alcocontrol.model.Plan
import com.alcocontrol.timeFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_plan.*
import org.jetbrains.anko.textColorResource
import org.threeten.bp.DateTimeException
import org.threeten.bp.LocalTime

@AndroidEntryPoint
class PlanFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private val navController by lazy { findNavController() }

    private val drinkDialog by lazy { DrinkDialog.newInstance() }

    private lateinit var plan: Plan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        plan = Plan()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        il_time.addTextMask("[00]{:}[00]")
        tv_drink.setOnClickListener {
            if (!drinkDialog.isAdded) {
                drinkDialog.show(childFragmentManager, drinkDialog.javaClass.simpleName)
            }
        }
        il_volume.addTextChangedListener {
            checkInput()
        }
        il_amount.addTextChangedListener {
            checkInput()
        }
        il_time.addTextChangedListener {
            checkInput()
        }
        tv_skip.setOnClickListener {
            navController.popBackStack(R.id.planFragment, true)
            navController.navigateOnce(R.id.mainFragment)
        }
        tv_choose.setOnClickListener {
            viewModel.plan.value = plan
            navController.popBackStack(R.id.planFragment, true)
            navController.navigateOnce(R.id.mainFragment)
        }
        viewModel.drink.observe(viewLifecycleOwner, {
            tv_drink.text = it.name
            checkInput()
        })
    }

    private fun checkInput() {
        try {
            val drink = viewModel.drink.value
            require(drink != null) {
                tv_drink_error.isVisible = true
                0
            }
            tv_drink_error.isVisible = false
            plan.drink = drink
            val volume = il_volume.text.toString().toInt()
            check(volume in 1..1000) {
                il_volume.showError()
                0
            }
            il_volume.hideError()
            plan.volume = volume
            plan.amount = il_amount.text.toString().toIntOrNull() ?: 0
            if (il_time.text.length >= 5) {
                val time = LocalTime.parse(il_time.text, timeFormatter)
                il_time.hideError()
                plan.setTime(time)
            }
            tv_choose.apply {
                backgroundTintList = null
                setTextColor(Color.WHITE)
                isEnabled = true
            }
            return
        } catch (e: DateTimeException) {
            il_time.showError()
        } catch (e: Throwable) {
        }
        tv_choose.apply {
            if (isEnabled) {
                backgroundTintList = ContextCompat.getColorStateList(context, R.color.colorHighlight)
                textColorResource = R.color.colorGray50
                isEnabled = false
            }
        }
    }

    companion object : Instance<PlanFragment>(PlanFragment::class.java)
}