package com.example.firebaseecom.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.CartViewBinding
import com.example.firebaseecom.model.ProductHomeModel

class ProductCategoryAdapter : RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder>() {
    var productList = mutableListOf<ProductHomeModel>()
    lateinit var cartViewBinding: CartViewBinding

    class MyViewHolder(val cartViewBinding: CartViewBinding) :
        RecyclerView.ViewHolder(cartViewBinding.root) {
        fun bind(productHomeModel: ProductHomeModel) {
            cartViewBinding.productHome = productHomeModel
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        cartViewBinding = DataBindingUtil.inflate(inflater, R.layout.cart_view, parent, false)
        return MyViewHolder(cartViewBinding)

    }

    override fun onBindViewHolder(holder: ProductCategoryAdapter.MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return productList.size
    }


}
