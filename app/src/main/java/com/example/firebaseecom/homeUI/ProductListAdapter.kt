@file:Suppress("DEPRECATION")

package com.example.firebaseecom.homeUI

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ProductListViewBinding
import com.example.firebaseecom.detailsUI.ProductDetailsActivity
import com.example.firebaseecom.model.ProductHomeModel
import java.io.Serializable

class ProductListAdapter(
    private val context: Context
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    private var productDetails: List<ProductHomeModel>? = listOf()

    private lateinit var productListViewBinding: ProductListViewBinding

    interface FirestoreOperations {
        fun addToWishlist(productModel: ProductHomeModel)
        fun removeFromWishlist(productModel: ProductHomeModel)
        fun checkInWishlist(id: Int): Boolean
    }

    class ProductViewHolder(
        private val productListViewBinding: ProductListViewBinding, val context: Context
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
        return ProductViewHolder(productListViewBinding, context)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productDetails?.get(position)
        holder.bind(product)
        Glide.with(context).load(product?.productImage).error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.homeProductView))
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("product", product as Serializable)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return productDetails!!.count()
    }
}