package com.amb.kevyorganizer.presentation.product

import android.Manifest
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.amb.core.data.Product
import com.amb.kevyorganizer.R
import com.amb.kevyorganizer.data.AddProductViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileInputStream

class AddProductFragment : Fragment() {

    private val addProductRoot by lazy { view?.findViewById(R.id.addProductRoot) as ConstraintLayout }
    private val ivProduct by lazy { view?.findViewById(R.id.ivProduct) as ImageView }
    private val edtProductName by lazy { view?.findViewById(R.id.edtProductName) as EditText }
    private val edtProductPrice by lazy { view?.findViewById(R.id.edtProductPrice) as EditText } //todo add currency mask
    private val edtProductDescription by lazy { view?.findViewById(R.id.edtProductDescription) as EditText }
    private val npProductAmount by lazy { view?.findViewById(R.id.npProductAmount) as NumberPicker }
    private val btnSaveProduct by lazy { view?.findViewById(R.id.btnSaveProduct) as FloatingActionButton }
    private val ivProductImage by lazy { view?.findViewById(R.id.ivProductImage) as ImageView }

    private val args: AddProductFragmentArgs by navArgs()

    private lateinit var viewModel: AddProductViewModel

    private var productId = 0L
    private var currentProduct = Product(
        name = "",
        ammount = 0,
        price = 0.0,
        imageFilePath = "",
        description = "",
        creationTime = 0,
        updateTime = 0
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddProductViewModel::class.java)
        observeViewModel()
        arguments?.let {
            productId = AddProductFragmentArgs.fromBundle(it).productId
            if (productId != 0L) {
                viewModel.getProduct(productId)
            }
        }
        initView()
    }

    private fun observeViewModel() {
        viewModel.saved.observe(this, Observer { hasSaved ->
            if (hasSaved) {
                showSnackBar("Produto Salvo")
                hideKeyboard()
                popBackStack()
            } else {
                showSnackBar("Algo deu errado")
            }
        })
        viewModel.currentProduct.observe(this, Observer { product ->
            product?.let {
                currentProduct = product
                bindCurrentProduct()
            }
        })
    }

    private fun bindCurrentProduct() {
        edtProductName.setText(currentProduct.name, TextView.BufferType.EDITABLE)
        edtProductPrice.setText(currentProduct.price.toString(), TextView.BufferType.EDITABLE)
        edtProductDescription.setText(currentProduct.description, TextView.BufferType.EDITABLE)
        ivProductImage.setImageBitmap(getBitmap(currentProduct.imageFilePath))
        npProductAmount.value = currentProduct.ammount.toInt()
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(addProductRoot.windowToken, 0)
    }

    private fun initView() {
        setupAmountNumberPicker()
        setupImageView()
        setupSaveProductButton()
    }

    private fun buildDeleteAlertDialog(context: Context) {
        if (productId != 0L) {
            AlertDialog.Builder(context)
                .setTitle("Excluir Produto")
                .setMessage("Tem certeza que deseja excluir o produto ${currentProduct.name}?")
                .setPositiveButton("Sim") { dialog: DialogInterface?, i: Int -> deleteCurrentProduct() }
                .setNegativeButton("Não") { dialog: DialogInterface?, _ -> }
                .create()
                .show()
        }
    }

    private fun deleteCurrentProduct() {
        viewModel.deleteProduct(currentProduct)
    }

    private fun setupAmountNumberPicker() {
        npProductAmount.apply {
            minValue = 1
            maxValue = 100
            value = 1
        }
    }

    private fun setupSaveProductButton() {
        btnSaveProduct.setOnClickListener {
            if (edtProductName.text.isNotEmpty() && edtProductPrice.text.isNotEmpty()) {
                saveCurrentProduct()
            } //todo notify empty fields
        }
    }

    private fun setupImageView() {
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

    private fun showSnackBar(text: String) {
        Snackbar.make(addProductRoot, text, Snackbar.LENGTH_SHORT)
            .setAction("Action", null)
            .show()
    }

    private fun popBackStack() {
        Navigation.findNavController(addProductRoot).popBackStack()
    }

    private fun saveCurrentProduct() {
        val time = System.currentTimeMillis()
        currentProduct.name = edtProductName.text.toString()
        currentProduct.price = edtProductPrice.text.toString().toDouble()
        currentProduct.updateTime = time
        if (currentProduct.id == 0L) {
            currentProduct.creationTime = time
        }
        viewModel.saveProduct(currentProduct)
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
