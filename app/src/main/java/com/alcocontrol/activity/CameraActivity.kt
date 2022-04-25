package com.alcocontrol.activity

import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.load
import com.alcocontrol.R
import com.alcocontrol.data.FileManager
import com.alcocontrol.data.Preferences
import com.alcocontrol.dateFormatter
import com.alcocontrol.extension.use
import com.alcocontrol.fragment.PreviewDialog
import com.alcocontrol.model.Alcohol
import com.alcocontrol.model.Snapshot
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class CameraActivity : BaseActivity(), View.OnClickListener {

    @Inject
    lateinit var fileManager: FileManager

    @Inject
    lateinit var preferences: Preferences

    private val previewDialog by lazy { PreviewDialog.newInstance() }

    private lateinit var orientationListener: OrientationEventListener

    private lateinit var snapshot: Snapshot

    private var flash = Flash.AUTO

    private var mOrientation = Surface.ROTATION_0

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orientationListener = object : OrientationEventListener(applicationContext) {

            override fun onOrientationChanged(orientation: Int) {
                mOrientation = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
            }
        }
        flash = Flash.values()[preferences.flashMode]
        setContentView(R.layout.activity_camera)
        if (savedInstanceState != null) {
            cv_main.facing = savedInstanceState.getSerializable("facing") as Facing
        }
        cv_main.setLifecycleOwner(this)
        cv_main.setPictureSize { list ->
            // ratio 9 / 16
            list.filter { it.width.toFloat() / it.height < 0.6f }
        }
        cv_main.addCameraListener(object : CameraListener() {

            override fun onCameraOpened(options: CameraOptions) {
                cv_main.flash = flash
            }

            @Suppress("DEPRECATION")
            override fun onPictureTaken(result: PictureResult) {
                lifecycleScope.launch {
                    snapshot.rotation = result.rotation
                    snapshot.data = result.data
                    with(cl_container) {
                        isDrawingCacheEnabled = true
                        buildDrawingCache()
                        withContext(Dispatchers.IO) {
                            drawingCache?.use {
                                fileManager.drawOnPhoto(snapshot, it)
                            }
                        }
                        isDrawingCacheEnabled = false
                        destroyDrawingCache()
                    }
                    iv_shoot.isEnabled = true
                    loadLastPhoto()
                }
            }
        })
        fab_close.setOnClickListener(this)
        iv_flash.setOnClickListener(this)
        iv_history.setOnClickListener(this)
        iv_shoot.setOnClickListener(this)
        iv_camera.setOnClickListener(this)
        val volume = intent.getIntExtra(EXTRA_VOLUME, 0)
        var seconds = intent.getLongExtra(EXTRA_TIME, 0L)
        updateFlash()
        ic_main.update(intent.getFloatExtra(EXTRA_PPM, 0f))
        tc_main.update(seconds)
        vc_main.update(volume)
        al_all.update(intent.getSerializableExtra(EXTRA_MAP) as Map<Alcohol, Int>)
        tv_date.text = LocalDate.now().format(dateFormatter)
        loadLastPhoto()
        if (volume > 0) {
            lifecycleScope.launch {
                while (true) {
                    tc_main.update(seconds)
                    delay(TimeUnit.MINUTES.toMillis(1))
                    seconds += 60
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("facing", cv_main.facing)
    }

    override fun onResume() {
        super.onResume()
        orientationListener.enable()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab_close -> {
                finish()
            }
            R.id.iv_flash -> {
                flash = when (flash) {
                    Flash.OFF -> Flash.AUTO
                    Flash.AUTO ->  Flash.ON
                    else -> Flash.OFF
                }
                updateFlash()
                preferences.flashMode = flash.ordinal
            }
            R.id.iv_history -> {
                if (!previewDialog.isAdded) {
                    previewDialog.show(supportFragmentManager, previewDialog.javaClass.simpleName)
                }
            }
            R.id.iv_shoot -> {
                v.isEnabled = false
                snapshot = Snapshot(cv_main.facing == Facing.FRONT, mOrientation)
                cv_main.takePicture()
            }
            R.id.iv_camera -> {
                cv_main.toggleFacing()
            }
        }
    }

    private fun updateFlash() {
        iv_flash.setImageResource(when (flash) {
            Flash.OFF -> R.drawable.ic_lightning_stroke
            Flash.AUTO -> R.drawable.ic_lightning_white
            else -> R.drawable.ic_lightning_yellow
        })
    }

    private fun loadLastPhoto() {
        lifecycleScope.launch {
            val photo = withContext(Dispatchers.IO) {
                fileManager.getLatestPhoto()
            }
            iv_history.load(photo)
        }
    }

    override fun onPause() {
        orientationListener.disable()
        super.onPause()
    }

    override fun onDestroy() {
        cv_main.clearCameraListeners()
        super.onDestroy()
    }

    companion object {

        const val EXTRA_PPM = "ppm"

        const val EXTRA_TIME = "time"

        const val EXTRA_VOLUME = "volume"

        const val EXTRA_MAP = "map"
    }
}