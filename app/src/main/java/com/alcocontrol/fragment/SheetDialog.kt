package com.alcocontrol.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.alcocontrol.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class SheetDialog : BottomSheetDialogFragment() {

    override fun getTheme() = R.style.AppDialogStyle

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setOnShowListener {
            (it as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet)?.let { parent ->
                parent.layoutParams = parent.layoutParams.apply {
                    height = WindowManager.LayoutParams.MATCH_PARENT
                }
                BottomSheetBehavior.from(parent).apply {
                    isDraggable = false
                    state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
        return dialog
    }
}