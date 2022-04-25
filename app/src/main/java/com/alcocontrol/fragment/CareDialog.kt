package com.alcocontrol.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.alcocontrol.R
import kotlinx.android.synthetic.main.fragment_care.*

class CareDialog : DialogFragment() {

    override fun getTheme() = R.style.AppDialogStyle

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setDimAmount(0.75f)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_care, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_close.setOnClickListener {
            dismiss()
        }
        tv_ok.setOnClickListener {
            dismiss()
        }
    }

    companion object : Instance<CareDialog>(CareDialog::class.java)
}