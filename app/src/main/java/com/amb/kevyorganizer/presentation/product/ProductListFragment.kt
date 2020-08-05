package com.amb.kevyorganizer.presentation.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amb.kevyorganizer.R
import com.amb.kevyorganizer.data.ProductListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_product_list.*

class ProductListFragment : Fragment() {

    private val productListView by lazy { view?.findViewById(R.id.productListView) as RecyclerView }
    private val btnAddProduct by lazy { view?.findViewById(R.id.btnAddProduct) as FloatingActionButton }

    private val productListAdapter = ProductListAdapter(arrayListOf())
    private lateinit var viewModel: ProductListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        observeViewModel()
        initView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProducts()
    }

    private fun initView() {
        setupAddProductButton()
        setupListView()
    }

    private fun setupListView() {
        productListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productListAdapter
        }
    }

    private fun setupAddProductButton() {
        btnAddProduct.setOnClickListener { goToProductDetails() }
    }

    private fun observeViewModel() {
        viewModel.productsList.observe(this, Observer { list ->
            loadingView.visibility = View.GONE
            productListView.visibility = View.VISIBLE
            productListAdapter.updateProducts(list.sortedBy { it.updateTime })
        })
    }

    private fun goToProductDetails(id: Long = 0) {
        val action = ProductListFragmentDirections.actionGoToProduct()
        Navigation.findNavController(productListView).navigate(action)
    }

}
