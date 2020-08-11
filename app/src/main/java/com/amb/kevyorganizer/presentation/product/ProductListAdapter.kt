package com.amb.kevyorganizer.presentation.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amb.core.data.Product
import com.amb.kevyorganizer.R
import kotlinx.android.synthetic.main.item_product.view.*
import java.text.SimpleDateFormat
import java.util.*

class ProductListAdapter(
    private val products: ArrayList<Product>,
    private val actions: ListAction
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
    )

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val productLayout = view.productItemLayout
        private val productName = view.tvProductName
        private val productImage = view.ivProductImage //todo handle image
        private val productPrice = view.tvProducPrice
        private val productAmount = view.tvProductAmount //todo ajust layout
        private val productDate = view.tvProductDate

        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = product.price.toString()
            productAmount.text = product.ammount.toString()

            val sdf = SimpleDateFormat("dd/MM, HH:mm:ss", Locale.getDefault())
            val resultDate = Date(product.updateTime)
            val lastUpdate = "Última atualização: ${sdf.format(resultDate)}" //TODO extract to string
            productDate.text = lastUpdate

            productLayout.setOnClickListener { actions.onClick(product.id) }
        }

    }
}