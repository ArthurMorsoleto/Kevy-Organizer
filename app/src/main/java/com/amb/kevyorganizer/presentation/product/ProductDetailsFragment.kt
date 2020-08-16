package com.amb.kevyorganizer.presentation.product

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.amb.core.data.Product
import com.amb.kevyorganizer.R
import com.amb.kevyorganizer.data.ProductDetailsViewModel
import com.amb.kevyorganizer.presentation.camera.CameraActivity
import com.amb.kevyorganizer.presentation.getBitmap
import com.amb.kevyorganizer.presentation.hideKeyboard
import com.amb.kevyorganizer.presentation.showSnackBar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductDetailsFragment : Fragment() {

    private val addProductRoot by lazy { view?.findViewById(R.id.addProductRoot) as ConstraintLayout }
    private val ivProduct by lazy { view?.findViewById(R.id.ivProduct) as ImageView }
    private val edtProductName by lazy { view?.findViewById(R.id.edtProductName) as EditText }
    private val edtProductPrice by lazy { view?.findViewById(R.id.edtProductPrice) as EditText } //todo add currency mask
    private val edtProductDescription by lazy { view?.findViewById(R.id.edtProductDescription) as EditText }
    private val npProductAmount by lazy { view?.findViewById(R.id.npProductAmount) as NumberPicker }
    private val btnSaveProduct by lazy { view?.findViewById(R.id.btnSaveProduct) as FloatingActionButton }
    private val btnDeleteProduct by lazy { view?.findViewById(R.id.btnDeleteProduct) as FloatingActionButton }
    private val loadingView by lazy { view?.findViewById(R.id.loading) as ProgressBar }
    private val svProductDetails by lazy { view?.findViewById(R.id.svProductDetails) as ScrollView }

    private lateinit var viewModel: ProductDetailsViewModel

    private var productId = 0L
    private var currentProduct = Product(
        name = "",
        amount = 0,
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
        viewModel = ViewModelProviders.of(this).get(ProductDetailsViewModel::class.java)
        observeViewModel()
        getProductFromArguments()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (productId != 0L) {
            viewModel.getProduct(productId)
        } else {
            loadingView.visibility = View.GONE
            svProductDetails.visibility = View.VISIBLE
        }
    }

    private fun getProductFromArguments() {
        arguments?.let {
            productId = ProductDetailsFragmentArgs.fromBundle(it).productId
            if (productId != 0L) {
                viewModel.getProduct(productId)
            } else {
                loadingView.visibility = View.GONE
                svProductDetails.visibility = View.VISIBLE
            }
        }
    }

    private fun initView() {
        setupAmountNumberPicker()
        setupImageView()
        setupSaveProductButton()
        setupDeleteProductButton()
    }

    private fun observeViewModel() {
        viewModel.saved.observe(this, Observer { hasSaved ->
            if (hasSaved) {
                showSnackBar("Produto Salvo", addProductRoot) //todo extract string
                context?.let { hideKeyboard(it, addProductRoot) }
                popBackStack()
            } else {
                showSnackBar("Algo deu errado", addProductRoot) //todo extract string
            }
        })
        viewModel.currentProduct.observe(this, Observer { product ->
            product?.let {
                currentProduct = product
                bindCurrentProduct()
            }
        })
        viewModel.loading.observe(this, Observer { loading ->
            loadingView.visibility = if (loading) View.VISIBLE else View.GONE
            svProductDetails.visibility = if (loading) View.GONE else View.VISIBLE
        })
    }

    private fun bindCurrentProduct() {
        edtProductName.setText(currentProduct.name, TextView.BufferType.EDITABLE)
        edtProductPrice.setText(currentProduct.price.toString(), TextView.BufferType.EDITABLE)
        edtProductDescription.setText(currentProduct.description, TextView.BufferType.EDITABLE)
        npProductAmount.value = currentProduct.amount.toInt()
        ivProduct.setImageBitmap(getBitmap(currentProduct.imageFilePath))
    }

    private fun deleteCurrentProduct() {
        viewModel.deleteProduct(currentProduct)
    }

    private fun setupAmountNumberPicker() {
        npProductAmount.apply {
            minValue = MIN_AMOUNT_VALUE
            maxValue = MAX_AMOUNT_VALUE
        }
    }

    private fun setupSaveProductButton() {
        btnSaveProduct.setOnClickListener {
            if (edtProductName.text.isNotEmpty() && edtProductPrice.text.isNotEmpty()) {
                saveCurrentProduct()
            } //todo notify empty fields
        }
    }

    private fun setupDeleteProductButton() {
        btnDeleteProduct.setOnClickListener { context?.let { buildDeleteAlertDialog(it) } }
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
        }
    }

    private fun saveCurrentProduct() {
        val time = System.currentTimeMillis()
        currentProduct.name = edtProductName.text.toString()
        currentProduct.price = edtProductPrice.text.toString().toDouble()
        currentProduct.description = edtProductDescription.text.toString()
        currentProduct.amount = npProductAmount.value.toLong()
        currentProduct.updateTime = time
        if (currentProduct.id == 0L) {
            currentProduct.creationTime = time
        }
        viewModel.saveProduct(currentProduct)
    }

    private fun buildDeleteAlertDialog(context: Context) {
        if (productId != 0L) {
            AlertDialog.Builder(context)
                .setTitle("Excluir Produto") //todo extract strings
                .setMessage("Tem certeza que deseja excluir o produto ${currentProduct.name}?")
                .setPositiveButton("Sim") { dialog: DialogInterface?, i: Int -> deleteCurrentProduct() }
                .setNegativeButton("Não") { dialog: DialogInterface?, _ -> }
                .create()
                .show()
        }
    }

    private fun popBackStack() {
        Navigation.findNavController(addProductRoot).popBackStack()
    }

    private fun goToCamera() {
        startActivityForResult(Intent(activity, CameraActivity::class.java), CAMERA_REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == CAMERA_REQUEST_CODE) {
            val imgFilePath = data?.getStringExtra(CAMERA_KEY)
            imgFilePath?.let {
                currentProduct.imageFilePath = imgFilePath
                ivProduct.setImageBitmap(getBitmap(imgFilePath))
            }
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 200
        const val CAMERA_REQUEST_CODE = 205
        const val CAMERA_KEY = "CAMERA_FILE_PATH"
        const val MIN_AMOUNT_VALUE = 1
        const val MAX_AMOUNT_VALUE = 100
    }

}