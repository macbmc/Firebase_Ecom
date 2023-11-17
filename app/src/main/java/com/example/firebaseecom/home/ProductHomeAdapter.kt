package com.example.firebaseecom.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ProductListViewBinding

import com.example.firebaseecom.model.ProductHomeModel

class ProductHomeAdapter(
    private val navigateClass: HomeActivity.NavigateClass
) : RecyclerView.Adapter<ProductHomeAdapter.ProductViewHolder>() {
    private var productDetails: List<ProductHomeModel>? = listOf()

    private lateinit var productListViewBinding: ProductListViewBinding

    interface NavigationInterface {
        fun navigateToDetails(productModel: ProductHomeModel)
    }

    class ProductViewHolder(
        private val productListViewBinding: ProductListViewBinding
    ) : RecyclerView.ViewHolder(productListViewBinding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(productModel: ProductHomeModel?) {
            productListViewBinding.productDetails = productModel
        }

    }

    fun setProduct(productDetails: List<ProductHomeModel>?) {
        this.productDetails = productDetails
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        productListViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.product_list_view, parent, false)
        return ProductViewHolder(productListViewBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productDetails?.get(position)
        holder.bind(product)
        Glide.with(holder.itemView).load(product?.productImage).error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.homeProductView))
        holder.itemView.setOnClickListener {
            navigateClass.navigateToDetails(product!!)

        }

    }

    override fun getItemCount(): Int {
        return productDetails!!.count()
    }
}