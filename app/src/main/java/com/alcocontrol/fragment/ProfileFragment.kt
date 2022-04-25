package com.alcocontrol.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.alcocontrol.ADULT_AGE
import com.alcocontrol.R
import com.alcocontrol.activity.Navigator
import com.alcocontrol.data.Preferences
import com.alcocontrol.dateFormatter
import com.alcocontrol.extension.cutTrailingZeros
import com.alcocontrol.model.Gender
import com.alcocontrol.model.Profile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.include_close.*
import org.jetbrains.anko.textColorResource
import org.threeten.bp.DateTimeException
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : SheetDialog() {

    @Inject
    lateinit var preferences: Preferences

    @Inject
    lateinit var navigator: Navigator

    private val policyDialog by lazy {
        DocDialog.newInstance(
            DocDialog.TITLE to R.string.privacy_policy,
            DocDialog.PATH to "policy.txt"
        )
    }

    private lateinit var profile: Profile

    private var isNewProfile = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profile = preferences.profile ?: Profile()
        isNewProfile = !preferences.contains(preferences::profile)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        il_birthday.addTextMask("[00]{.}[00]{.}[0000]")
        tv_agree.tag = false
        if (isNewProfile) {
            tv_title.text = getString(R.string.registration)
            fab_close.isVisible = false
            tv_agree.isVisible = true
            tv_policy.isVisible = true
            tv_action.text = getString(R.string._continue)
        } else {
            tv_title.text = getString(R.string.change_data)
            il_name.text = profile.name
            il_weight.text = profile.weight.toString().cutTrailingZeros()
            il_birthday.text = profile.birthday.format(dateFormatter)
            markGender(profile.gender)
            tv_action.text = getString(R.string.save)
            checkInput()
        }
        fab_close.setOnClickListener {
            dismiss()
        }
        il_name.addTextChangedListener {
            checkInput()
        }
        il_weight.addTextChangedListener {
            checkInput()
        }
        il_birthday.addTextChangedListener {
            checkInput()
        }
        tv_male.setOnClickListener {
            profile.gender = Gender.MALE
            markGender(Gender.MALE)
        }
        tv_female.setOnClickListener {
            profile.gender = Gender.FEMALE
            markGender(Gender.FEMALE)
        }
        tv_agree.setOnClickListener {
            it.tag = !(it.tag as Boolean)
            val icon = if (it.tag as Boolean) R.drawable.ic_box_active else R.drawable.ic_box_inactive
            tv_agree.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
            checkInput()
        }
        tv_policy.setOnClickListener {
            if (!policyDialog.isAdded) {
                policyDialog.show(childFragmentManager, policyDialog.javaClass.simpleName)
            }
        }
        tv_action.setOnClickListener {
            preferences.profile = profile
            if (isNewProfile) {
                navigator.navigateNext()
            } else {
                dismiss()
            }
        }
    }

    private fun markGender(gender: Gender) {
        when (gender) {
            Gender.MALE -> {
                tv_male.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_male_active, 0, 0, 0)
                tv_male.textColorResource = R.color.colorRed
                tv_female.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_female, 0, 0, 0)
                tv_female.textColorResource = R.color.colorGray30
            }
            else -> {
                tv_male.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_male, 0, 0, 0)
                tv_male.textColorResource = R.color.colorGray30
                tv_female.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_female_active, 0, 0, 0)
                tv_female.textColorResource = R.color.colorRed
            }
        }
    }

    private fun checkInput() {
        try {
            val name = il_name.text.toString()
            require(name.isNotBlank()) {
                il_name.showError()
                0
            }
            il_name.hideError()
            profile.name = name
            val weight = il_weight.text.toString().toFloat()
            check(weight > 0) {
                il_weight.showError()
                0
            }
            il_weight.hideError()
            profile.weight = weight
            val date = LocalDate.parse(il_birthday.text, dateFormatter)
            val age = ChronoUnit.YEARS.between(date, LocalDate.now())
            il_birthday.setSuffix(age)
            check(age >= ADULT_AGE) {
                il_birthday.showError(R.string.error_birthday)
                0
            }
            il_birthday.hideError()
            profile.birthday = date
            require(profile.hasGender)
            require(!isNewProfile || tv_agree.tag as Boolean)
            tv_action.apply {
                backgroundTintList = null
                setTextColor(Color.WHITE)
                isEnabled = true
            }
            return
        } catch (e: DateTimeException) {
            if (il_birthday.text.length >= 10) {
                il_birthday.showError(R.string.error_birthday_format)
            }
        } catch (e: Throwable) {
        }
        tv_action.apply {
            if (isEnabled) {
                backgroundTintList = ContextCompat.getColorStateList(context, R.color.colorHighlight)
                textColorResource = R.color.colorGray50
                isEnabled = false
            }
        }
    }

    companion object : Instance<ProfileFragment>(ProfileFragment::class.java)
}