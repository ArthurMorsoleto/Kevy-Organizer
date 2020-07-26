package com.amb.kevyorganizer.kevy.product

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.amb.kevyorganizer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.io.File
import java.io.FileInputStream

class AddProductFragment : Fragment() {

    private val btnSaveProduct by lazy { view?.findViewById(R.id.btnSaveProduct) as FloatingActionButton }
    private val npProductAmount by lazy { view?.findViewById(R.id.npProductAmount) as NumberPicker }
    private val ivProduct by lazy { view?.findViewById(R.id.ivProduct) as ImageView }

    private val args: AddProductFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnSaveProduct.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        npProductAmount.apply {
            minValue = 1
            maxValue = 100
            value = 1
        }
        ivProduct.apply {
            setOnClickListener {
                if (checkCameraPermission()) {
                    goToCamera()
                } else {
                    requestCameraPermission()
                }
            }
            if (args.productImageFile.isNotBlank()) {
                setProductImage(getBitmap(args.productImageFile))
            }
        }
    }

    private fun goToCamera() {
        val option = AddProductFragmentDirections.actionGoToCamera()
        Navigation.findNavController(addProductRoot).navigate(option)
    }

    private fun setProductImage(bitmap: Bitmap?) {
        bitmap?.let {
            ivProduct.setImageBitmap(bitmap)
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

    private fun checkCameraPermission(): Boolean {
        activity?.applicationContext?.let {
            return ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    private fun requestCameraPermission() {
        activity?.let {
            ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
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
                goToCamera()
            }
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 200
    }

}
