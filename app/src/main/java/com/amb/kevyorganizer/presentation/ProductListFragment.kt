package com.amb.kevyorganizer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.amb.kevyorganizer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductListFragment : Fragment() {

    private val productListView by lazy { view?.findViewById(R.id.productListView) as RecyclerView }
    private val btnAddProduct by lazy { view?.findViewById(R.id.btnAddProduct) as FloatingActionButton }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btnAddProduct.setOnClickListener {
            goToProductDetails()
        }
    }

    private fun goToProductDetails(id: Long = 0) {
        val action = ProductListFragmentDirections.actionGoToProduct()
        Navigation.findNavController(productListView).navigate(action)
    }

}
