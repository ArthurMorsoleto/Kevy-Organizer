package com.amb.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import kotlinx.android.synthetic.main.view_kevy_camera.view.*
import java.io.File
import java.io.FileOutputStream

class KevyCameraView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var fotoapparat: Fotoapparat

    init {
        View.inflate(context, R.layout.view_kevy_camera, this)
        fotoapparat = Fotoapparat(
            context = context,
            view = cameraView,
            scaleType = ScaleType.CenterCrop,
            lensPosition = back(),
            logger = loggers(logcat())
        )
    }

    fun start() {
        fotoapparat.start()
    }

    fun stop() {
        fotoapparat.stop()
    }

    fun capturePhoto(
        onCaptureSuccess: (String) -> Unit,
        folderName: String
    ) {
        val photoFilePath = createPhotoFilePath(folderName)
        fotoapparat.takePicture()
            .toBitmap()
            .whenAvailable {
                it?.bitmap?.let { bitmap ->
                    val photoBitmap = adjustRotation(bitmap)
                    try {
                        saveCapturedPhoto(photoFilePath, photoBitmap)
                        onCaptureSuccess(photoFilePath)
                    } catch (e: Exception) {
                    } finally {
                        bitmap.recycle()
                        photoBitmap.recycle()
                    }
                }
            }
    }

    private fun adjustRotation(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        val degrees = 90F
        matrix.postRotate(degrees)
        matrix.preScale(1F, 1F)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun saveCapturedPhoto(photoFilePath: String, bitmap: Bitmap) {
        val out = FileOutputStream(File(photoFilePath))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
    }

    private fun createPhotoFilePath(folderName: String): String {
        val filesDir = "${context.filesDir}"
        val fileName = "${System.currentTimeMillis()}.jpg"
        val fullPath = if (folderName.isBlank()) {
            "$filesDir${File.separator}$fileName"
        } else {
            val folderPath = "$filesDir${File.separator}$folderName"
            with(File(folderPath)) {
                if (!exists())
                    mkdir()
            }
            "${folderPath}${File.separator}${fileName}"
        }
        return fullPath
    }

}
