package com.alcocontrol.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.alcocontrol.R
import com.alcocontrol.activity.MainViewModel
import kotlinx.android.synthetic.main.fragment_doc.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

open class DocDialog : DialogFragment() {

    protected val viewModel: MainViewModel by activityViewModels()

    override fun getTheme() = R.style.AppDialogStyle

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setDimAmount(0.5f)
        dialog.setOnShowListener {
            dialog.window?.attributes = dialog.window?.attributes?.apply {
                height = (resources.displayMetrics.heightPixels * 0.8f).roundToInt()
            }
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_doc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = requireArguments()
        fab_close.setOnClickListener {
            dismiss()
        }
        tv_title.text = getString(args.getInt(TITLE))
        viewLifecycleOwner.lifecycleScope.launch {
            tv_text.text = viewModel.readText(args.getString(PATH)!!)
        }
    }

    companion object : Instance<DocDialog>(DocDialog::class.java) {

        const val TITLE = "title"

        const val PATH = "path"
    }
}