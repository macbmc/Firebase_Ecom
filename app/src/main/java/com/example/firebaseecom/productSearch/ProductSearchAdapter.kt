package com.example.firebaseecom.productSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.CartViewBinding
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.asMap
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
class ProductSearchAdapter(val productSearchClass: ProductSearchActivity.ProductSearchClass,val langId:String) :
    RecyclerView.Adapter<ProductSearchAdapter.MyViewHolder>() {
    interface ProductCategoryInterface {
        fun addToCart(productHomeModel: ProductHomeModel)
        fun navToDetails(productHomeModel: ProductHomeModel)
    }

    var productList = mutableListOf<ProductHomeModel>()
    private lateinit var cartViewBinding: CartViewBinding

    inner class MyViewHolder(private val cartViewBinding: CartViewBinding,val langId:String) :
        RecyclerView.ViewHolder(cartViewBinding.root) {
        fun bind(productHomeModel: ProductHomeModel) {
            cartViewBinding.apply {
                productTitleText.text=productHomeModel.productTitle.asMap()[langId].toString()
                productHome = productHomeModel
                deleteBtn.setOnClickListener {
                    productSearchClass.addToCart(productHomeModel)
                }
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        cartViewBinding = DataBindingUtil.inflate(inflater, R.layout.cart_view, parent, false)
        return MyViewHolder(cartViewBinding,langId)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
        cartViewBinding.deleteBtn.setImageResource(+R.drawable.ic_cart)
        Glide.with(holder.itemView)
            .load(product.productImage)
            .error(R.drawable.error_image)
            .into(holder.itemView.findViewById(R.id.productImage))
        holder.itemView.setOnClickListener {
            productSearchClass.navToDetails(product)
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProducts(productlist: List<ProductHomeModel>) {
        this.productList = productlist.toMutableList()
        notifyDataSetChanged()
    }


}

