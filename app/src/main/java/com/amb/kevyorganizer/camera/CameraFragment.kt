package com.amb.kevyorganizer.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.amb.camera.KevyCameraView
import com.amb.kevyorganizer.R
import java.io.File
import java.io.FileInputStream

class CameraFragment : Fragment() {

    private val btnCapture by lazy { view?.findViewById(R.id.captureButton) as ImageView }
    private val camera by lazy { view?.findViewById(R.id.ambCameraView) as KevyCameraView }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkCameraPermission()) {
            startCamera()
            setupCaptureButton()
        } else {
            requestCameraPermission()
        }
    }

    private fun startCamera() {
        camera.start()
    }

    override fun onDetach() {
        super.onDetach()
        camera.stop()
    }

    private fun checkCameraPermission(): Boolean {
        activity?.applicationContext?.let {
            return ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    private fun requestCameraPermission() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it, arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                startCamera()
                setupCaptureButton()
            }
        }
    }

    private fun getBitmap(path: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(File(path)), null, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun setupCaptureButton() {
        btnCapture.setOnClickListener {
            camera.capturePhoto(
                onCaptureSuccess = { path ->
                    val productBitmap = getBitmap(path)
                    //TODO handle saving product image
                },
                folderName = FOLDER_NAME
            )
        }
    }

    companion object {
        const val FOLDER_NAME = "KevyOganizerFolder"
        private const val PERMISSION_REQUEST_CODE = 200
    }

}
