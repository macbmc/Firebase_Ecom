package com.example.firebaseecom.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ProductListViewBinding
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.ProductOffersModel
import com.example.firebaseecom.model.asMap

class ProductHomeAdapter(
    private val context: Context,
    private val navigateClass: HomeActivity.NavigateClass,
    private val langId: String
) : RecyclerView.Adapter<ProductHomeAdapter.ProductViewHolder>() {
    private var productDetails: List<ProductHomeModel>? = listOf()
    private var offerDetails: List<ProductOffersModel>? = listOf()

    private lateinit var productListViewBinding: ProductListViewBinding

    interface NavigationInterface {
        fun navigateToDetails(productModel: ProductHomeModel, offersModelList: List<ProductOffersModel>)
    }

    inner class ProductViewHolder(
        private val productListViewBinding: ProductListViewBinding, private val langId: String
    ) : RecyclerView.ViewHolder(productListViewBinding.root) {

        fun bind(productModel: ProductHomeModel?) {
            val productTitleLang = productModel?.productTitle?.asMap()
            productListViewBinding.apply {
                productDetails = productModel
                homeProductTitle.text = productTitleLang?.get(langId).toString()
                for (offer in offerDetails!!) {
                    if (offer.productId == productModel?.productId) {
                        Log.d("offerDatadiscount", productModel?.productId.toString())
                        Glide.with(itemView)
                            .load(offer.offerImage)
                            .into(discountImage)
                    }

                }


            }
        }

    }

    fun setProduct(productDetails: List<ProductHomeModel>?, offerData: List<ProductOffersModel>) {
        this.productDetails = productDetails
        this.offerDetails = offerData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        productListViewBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.product_list_view, parent, false)
        return ProductViewHolder(productListViewBinding, langId)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productDetails?.get(position)
        holder.bind(product)
        Glide.with(holder.itemView).load(product?.productImage).error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.homeProductView))
        holder.itemView.setOnClickListener {
            navigateClass.navigateToDetails(product!!, offerDetails!!)


        }

    }

    override fun getItemCount(): Int {
        return productDetails!!.count()
    }
}