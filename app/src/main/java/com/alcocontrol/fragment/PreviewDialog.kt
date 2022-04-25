package com.alcocontrol.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.alcocontrol.R
import com.alcocontrol.data.FileManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_preview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class PreviewDialog : SheetDialog() {

    @Inject
    lateinit var fileManager: FileManager

    private var photo: File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab_close.setOnClickListener {
            dismiss()
        }
        iv_share.setOnClickListener {
            try {
                val context = requireContext()
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.file_provider", photo!!)
                startActivity(
                    ShareCompat.IntentBuilder(context)
                        .setType("image/*")
                        .setStream(uri)
                        .setText(il_caption.text)
                        .intent
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                )
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            photo = withContext(Dispatchers.IO) {
                fileManager.getLatestPhoto()
            }
            zv_preview.load(photo)
        }
    }

    companion object : Instance<PreviewDialog>(PreviewDialog::class.java)
}