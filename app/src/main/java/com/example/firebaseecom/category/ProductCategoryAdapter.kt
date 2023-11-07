package com.example.firebaseecom.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.CartViewBinding
import com.example.firebaseecom.model.ProductHomeModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
@ActivityScoped
class ProductCategoryAdapter(val productCategoryClass: ProductCategoryActivity.ProductCategoryClass) : RecyclerView.Adapter<ProductCategoryAdapter.MyViewHolder>() {
    interface ProductCategoryInterface{
        fun addToCart(productHomeModel: ProductHomeModel)
        fun navToDetails(productHomeModel: ProductHomeModel)
    }
    var productList = mutableListOf<ProductHomeModel>()
    lateinit var cartViewBinding: CartViewBinding

    inner class MyViewHolder(val cartViewBinding: CartViewBinding) :
        RecyclerView.ViewHolder(cartViewBinding.root) {
        fun bind(productHomeModel: ProductHomeModel) {
            cartViewBinding.productHome = productHomeModel
            cartViewBinding.deleteBtn.setOnClickListener {
                productCategoryClass.addToCart(productHomeModel)
            }
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

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        val product=productList[position]
        holder.bind(product)
        cartViewBinding.deleteBtn.setImageResource(+R.drawable.ic_cart)
        Glide.with(holder.itemView)
            .load(product.productImage)
            .error(R.drawable.error_image)
            .into(holder.itemView.findViewById(R.id.productImage))
        holder.itemView.setOnClickListener {
            productCategoryClass.navToDetails(product)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProducts(productlist:List<ProductHomeModel>)
    {
        this.productList=productlist.toMutableList()
        notifyDataSetChanged()
    }


}
