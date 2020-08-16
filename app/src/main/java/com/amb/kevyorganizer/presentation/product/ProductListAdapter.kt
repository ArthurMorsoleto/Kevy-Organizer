package com.amb.kevyorganizer.presentation.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amb.core.data.Product
import com.amb.kevyorganizer.R
import com.amb.kevyorganizer.presentation.getProgressDrawable
import com.amb.kevyorganizer.presentation.loadImage
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
        private val productImage = view.ivProductImage
        private val productPrice = view.tvProducPrice
        private val productAmount = view.tvProductAmount
        private val productDate = view.tvProductDate

        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = product.price.toString()
            productAmount.text = this.itemView.context.getString(R.string.product_amount, product.amount.toString())

            if (product.imageFilePath.isNotBlank()) {
                productImage.loadImage(product.imageFilePath, getProgressDrawable(this.itemView.context))
            }

            val sdf = SimpleDateFormat("dd/MM-HH:mm", Locale.getDefault())
            val resultDate = Date(product.updateTime)
            val lastUpdate = this.itemView.context.getString(R.string.product_date, sdf.format(resultDate))
            productDate.text = lastUpdate

            productLayout.setOnClickListener { actions.onClick(product.id) }
        }

    }
}