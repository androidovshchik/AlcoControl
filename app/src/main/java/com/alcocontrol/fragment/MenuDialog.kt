package com.alcocontrol.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.view.isVisible
import com.alcocontrol.R
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.include_close.*
import org.jetbrains.anko.browse

class MenuDialog : SheetDialog(), View.OnClickListener {

    private val profileDialog by lazy { ProfileFragment.newInstance() }

    private val languageDialog by lazy { LanguageDialog.newInstance() }

    private val notificationDialog by lazy { NotificationDialog.newInstance() }

    private val faqDialog by lazy { FAQDialog.newInstance() }

    private val feedbackDialog by lazy {
        FeedbackDialog.newInstance(
            FeedbackDialog.TITLE to R.string.feedback,
            FeedbackDialog.TITLE_SIZE to 25f,
            FeedbackDialog.CAPTION to R.string.your_question
        )
    }

    private val offerDialog by lazy {
        FeedbackDialog.newInstance(
            FeedbackDialog.TITLE to R.string.offers,
            FeedbackDialog.TITLE_SIZE to 18f,
            FeedbackDialog.CAPTION to R.string.your_offers
        )
    }

    private val termsDialog by lazy {
        DocDialog.newInstance(
            DocDialog.TITLE to R.string.terms_of_use,
            DocDialog.PATH to "terms.txt"
        )
    }

    private val policyDialog by lazy {
        DocDialog.newInstance(
            DocDialog.TITLE to R.string.privacy_policy,
            DocDialog.PATH to "policy.txt"
        )
    }

    private val rulesDialog by lazy {
        DocDialog.newInstance(
            DocDialog.TITLE to R.string.project_rules,
            DocDialog.PATH to "about.txt"
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_close.isVisible = true
        fab_close.setOnClickListener(this)
        db_change_data.setOnClickListener(this)
        db_change_lang.setOnClickListener(this)
        db_push_set.setOnClickListener(this)
        db_rate_app.setOnClickListener(this)
        db_share_app.setOnClickListener(this)
        db_faq.setOnClickListener(this)
        db_feedback.setOnClickListener(this)
        db_offers.setOnClickListener(this)
        tv_agreement.setOnClickListener(this)
        tv_policy.setOnClickListener(this)
        tv_rules.setOnClickListener(this)
        tv_developer.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val context = requireContext()
        when (v.id) {
            R.id.fab_close -> {
                dismiss()
            }
            R.id.db_change_data -> {
                if (!profileDialog.isAdded) {
                    profileDialog.show(childFragmentManager, profileDialog.javaClass.simpleName)
                }
            }
            R.id.db_change_lang -> {
                if (!languageDialog.isAdded) {
                    languageDialog.show(childFragmentManager, languageDialog.javaClass.simpleName)
                }
            }
            R.id.db_push_set -> {
                if (!notificationDialog.isAdded) {
                    notificationDialog.show(childFragmentManager, notificationDialog.javaClass.simpleName)
                }
            }
            R.id.db_rate_app -> {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("market://details?id=${context.packageName}")
                    })
                } catch (e: Throwable) {
                    context.browse("https://play.google.com/store/apps/dev?id=${context.packageName}", true)
                }
            }
            R.id.db_share_app -> {
                try {
                    startActivity(
                        ShareCompat.IntentBuilder(context)
                            .setType("text/plain")
                            .setText(context.packageName)
                            .intent
                    )
                } catch (e: Throwable) {
                }
            }
            R.id.db_faq -> {
                if (!faqDialog.isAdded) {
                    faqDialog.show(childFragmentManager, faqDialog.javaClass.simpleName)
                }
            }
            R.id.db_feedback -> {
                if (!feedbackDialog.isAdded) {
                    feedbackDialog.show(childFragmentManager, feedbackDialog.javaClass.simpleName)
                }
            }
            R.id.db_offers -> {
                if (!offerDialog.isAdded) {
                    offerDialog.show(childFragmentManager, offerDialog.javaClass.simpleName)
                }
            }
            R.id.tv_agreement -> {
                if (!termsDialog.isAdded) {
                    termsDialog.show(childFragmentManager, termsDialog.javaClass.simpleName)
                }
            }
            R.id.tv_policy -> {
                if (!policyDialog.isAdded) {
                    policyDialog.show(childFragmentManager, policyDialog.javaClass.simpleName)
                }
            }
            R.id.tv_rules -> {
                if (!rulesDialog.isAdded) {
                    rulesDialog.show(childFragmentManager, rulesDialog.javaClass.simpleName)
                }
            }
            R.id.tv_developer -> {
                val developer = "5700313618786177705"
                try {
                    startActivity(Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("search?q=pub:$developer")
                    })
                } catch (e: Throwable) {
                    context.browse("https://play.google.com/store/apps/dev?id=$developer", true)
                }
            }
        }
    }

    companion object : Instance<MenuDialog>(MenuDialog::class.java)
}