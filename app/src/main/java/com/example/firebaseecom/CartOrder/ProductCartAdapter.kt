package com.example.firebaseecom.CartOrder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.CartViewBinding
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.ProductOffersModel
import com.example.firebaseecom.model.asMap

class ProductCartAdapter(
    val activityFunctionClass: ProductListActivity.ActivityFunctionClass,
    val langId: String
) : RecyclerView.Adapter<ProductCartAdapter.MyViewHolder>() {
    interface ActivityFunctionInterface {

        fun navigateToDetails(productHomeModel: ProductHomeModel, offersModelList: List<ProductOffersModel>)
        fun addTotalPrice(productList: List<ProductHomeModel>)
        fun deleteFromCart(productHomeModel: ProductHomeModel, position: Int)
    }

    var productList: MutableList<ProductHomeModel> = mutableListOf()
    private var offerDetails: List<ProductOffersModel>? = listOf()
    private lateinit var cartViewBinding: CartViewBinding
    override fun onCreateViewHolder(

        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        cartViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.cart_view, parent, false)
        return MyViewHolder(cartViewBinding, langId)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val productHome = productList[position]
        holder.bind(productHome, position)
        Glide.with(holder.itemView)
            .load(productHome.productImage)
            .error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.productImage))
        holder.itemView.setOnClickListener {
            activityFunctionClass.navigateToDetails(productHome,offerDetails!!)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProduct(productList: List<ProductHomeModel>, offerData: List<ProductOffersModel>) {
        this.productList = productList.toMutableList()
        notifyDataSetChanged()
        this.offerDetails = offerData
        activityFunctionClass.addTotalPrice(productList)
    }

    inner class MyViewHolder(private val cartViewBinding: CartViewBinding, val langId: String) :
        RecyclerView.ViewHolder(cartViewBinding.root) {
        fun bind(productHomeModel: ProductHomeModel, position: Int) {
            cartViewBinding.apply {
                Log.d("cartTitle", langId)
                productTitleText.text = productHomeModel.productTitle.asMap()[langId].toString()
                productHome = productHomeModel
                deleteBtn.setOnClickListener {
                    activityFunctionClass.deleteFromCart(productHomeModel, position)
                    productList.removeAt(bindingAdapterPosition)
                    notifyItemRemoved(bindingAdapterPosition)
                    activityFunctionClass.addTotalPrice(productList)

                }
            }


        }

    }

}


