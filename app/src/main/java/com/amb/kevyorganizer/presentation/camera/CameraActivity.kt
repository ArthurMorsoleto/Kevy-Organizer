package com.amb.kevyorganizer.presentation.camera

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.amb.camera.KevyCameraView
import com.amb.kevyorganizer.R
import com.amb.kevyorganizer.presentation.product.ProductDetailsFragment.Companion.CAMERA_KEY
import com.amb.kevyorganizer.presentation.product.ProductDetailsFragment.Companion.CAMERA_REQUEST_CODE

class CameraActivity : AppCompatActivity() {

    private val captureButton by lazy { findViewById<ImageView>(R.id.captureButton) }
    private val kevyCameraView by lazy { findViewById<KevyCameraView>(R.id.kevyCameraView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        startCamera()
        setupCaptureButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        kevyCameraView.stop()
    }

    private fun startCamera() {
        kevyCameraView.start()
    }

    private fun setupCaptureButton() {
        captureButton.setOnClickListener {
            kevyCameraView.capturePhoto(
                onCaptureSuccess = { path -> handleOnCaptureSuccess(path) },
                folderName = FOLDER_NAME
            )
        }
    }

    private fun handleOnCaptureSuccess(path: String) {
        setResult(CAMERA_REQUEST_CODE, Intent().apply { putExtra(CAMERA_KEY, path) })
        finish()
    }

    companion object {
        const val FOLDER_NAME = "KevyOrganizerFolder"
    }

}
