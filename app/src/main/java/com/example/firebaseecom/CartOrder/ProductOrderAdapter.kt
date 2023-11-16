package com.example.firebaseecom.CartOrder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.CartViewBinding
import com.example.firebaseecom.databinding.OrderViewBinding
import com.example.firebaseecom.model.ProductHomeModel
import javax.inject.Inject

class ProductOrderAdapter @Inject constructor(
) : RecyclerView.Adapter<ProductOrderAdapter.MyViewHolder>() {

    interface navInterface{
        fun navToEditOrder()
        fun navToProductDetails()
    }

    var productList: MutableList<ProductHomeModel> = mutableListOf()
    private lateinit var orderViewBinding: OrderViewBinding
    override fun onCreateViewHolder(

        parent: ViewGroup,
        viewType: Int
    ): ProductOrderAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        orderViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.order_view, parent, false)
        return MyViewHolder(orderViewBinding)

    }

    override fun onBindViewHolder(holder: ProductOrderAdapter.MyViewHolder, position: Int) {
        val productHome = productList[position]
        holder.bind(productHome,position)
        Glide.with(holder.itemView)
            .load(productHome.productImage)
            .error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.productImage))
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProduct(productList: List<ProductHomeModel>) {
        this.productList = productList.toMutableList()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val orderViewBinding: OrderViewBinding) :
        RecyclerView.ViewHolder(orderViewBinding.root) {
        fun bind(productHomeModel: ProductHomeModel,position:Int) {
            orderViewBinding.productHome = productHomeModel
            orderViewBinding.editOrderBtn.setOnClickListener {

            }
        }

    }

}


