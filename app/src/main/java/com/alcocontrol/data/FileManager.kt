package com.alcocontrol.data

import android.content.Context
import android.graphics.*
import androidx.annotation.WorkerThread
import com.alcocontrol.datetimeFormatter
import com.alcocontrol.extension.use
import com.alcocontrol.model.Snapshot
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.LocalDateTime
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FileManager @Inject constructor(@ApplicationContext context: Context) {

    private val externalDir: File? = context.getExternalFilesDir(null)

    private val internalDir: File = context.filesDir

    private val photosDir: File
        get() = File(internalDir, "photos").apply { mkdirs() }

    fun createPhoto(): File {
        val now = LocalDateTime.now()
        return File(photosDir, "${now.format(datetimeFormatter)}.jpg")
    }

    @WorkerThread
    fun drawOnPhoto(snapshot: Snapshot, frame: Bitmap) {
        try {
            val photo = BitmapFactory.decodeByteArray(snapshot.data, 0, snapshot.data.size)
            photo.use {
                val matrix = Matrix()
                var width = photo.width
                var height = photo.height
                if (snapshot.rotation % 180 != 0) {
                    width = photo.height
                    height = photo.width
                }
                val scale = if (width < height) {
                    frame.width.toFloat() / width
                } else {
                    frame.height.toFloat() / height
                }
                if (snapshot.isFront) {
                    if (snapshot.rotation % 180 != 0) {
                        matrix.preScale(scale, scale * -1f)
                    } else {
                        matrix.preScale(scale * -1f, scale)
                    }
                } else {
                    matrix.preScale(scale, scale)
                }
                if (snapshot.rotation % 360 != 0) {
                    matrix.postRotate(snapshot.rotation.toFloat())
                }
                val image = Bitmap.createBitmap(photo, 0, 0, photo.width, photo.height, matrix, true)
                image.use {
                    val result = Bitmap.createBitmap(frame.width, frame.height, Bitmap.Config.RGB_565)
                    result.use {
                        val canvas = Canvas(result)
                        canvas.drawBitmap(image, (frame.width - width * scale) / 2, (frame.height - height * scale) / 2, null)
                        canvas.drawBitmap(frame, 0f, 0f, null)
                        FileOutputStream(createPhoto()).use { output ->
                            result.compress(Bitmap.CompressFormat.JPEG, 90, output)
                            output.flush()
                        }
                    }
                }
            }
        } catch (e: Throwable) {
        }
    }

    @WorkerThread
    fun getLatestPhoto(): File? {
        return photosDir.listFiles()?.maxByOrNull { it.lastModified() }
    }

    companion object {

        /** @see https://developer.android.com/topic/performance/graphics/load-bitmap */
        private fun BitmapFactory.Options.calculateInSampleSize(reqWidth: Int, reqHeight: Int): Int {
            // Raw height and width of image
            val (height: Int, width: Int) = outHeight to outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                val halfHeight: Int = height / 2
                val halfWidth: Int = width / 2
                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }
    }
}
