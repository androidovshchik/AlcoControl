package com.alcocontrol.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.alcocontrol.R
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.android.synthetic.main.include_close.*

class FAQDialog : SheetDialog(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_close.setOnClickListener(this)
        db_faq_data.setOnClickListener(this)
        db_faq_update.setOnClickListener(this)
        db_faq_push.setOnClickListener(this)
        db_faq_feeling.setOnClickListener(this)
        db_faq_invite.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab_close -> {
                dismiss()
            }
            R.id.db_faq_data -> {
                db_answer_data.isVisible = !db_answer_data.isVisible
                db_faq_data.rotateIcon(if (db_answer_data.isVisible) 90f else 0f)
            }
            R.id.db_faq_update -> {
                db_answer_update.isVisible = !db_answer_update.isVisible
                db_faq_update.rotateIcon(if (db_answer_update.isVisible) 90f else 0f)
            }
            R.id.db_faq_push -> {
                db_answer_push.isVisible = !db_answer_push.isVisible
                db_faq_push.rotateIcon(if (db_answer_push.isVisible) 90f else 0f)
            }
            R.id.db_faq_feeling -> {
                db_answer_feeling.isVisible = !db_answer_feeling.isVisible
                db_faq_feeling.rotateIcon(if (db_answer_feeling.isVisible) 90f else 0f)
            }
            R.id.db_faq_invite -> {
                db_answer_invite.isVisible = !db_answer_invite.isVisible
                db_faq_invite.rotateIcon(if (db_answer_invite.isVisible) 90f else 0f)
            }
        }
    }

    companion object : Instance<FAQDialog>(FAQDialog::class.java)
}