package com.example.firebaseecom.CartOrder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.OrderViewBinding
import com.example.firebaseecom.model.ProductOrderModel
import com.example.firebaseecom.model.asMap

class ProductOrderAdapter(private val nav: navInterface,val langId: String
) : RecyclerView.Adapter<ProductOrderAdapter.MyViewHolder>() {

    interface navInterface{
        fun navToOrderView(productHomeModel: ProductOrderModel)
    }

    var productList = mutableListOf<ProductOrderModel>()
    private lateinit var orderViewBinding: OrderViewBinding
    override fun onCreateViewHolder(

        parent: ViewGroup,
        viewType: Int
    ): ProductOrderAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        orderViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.order_view, parent, false)
        return MyViewHolder(orderViewBinding,langId)

    }

    override fun onBindViewHolder(holder: ProductOrderAdapter.MyViewHolder, position: Int) {
        val productHome = productList[position]
        holder.bind(productHome,position)
        Glide.with(holder.itemView)
            .load(productHome.productImage)
            .error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.productImage))
        holder.itemView.setOnClickListener {
            nav.navToOrderView(productHome)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProduct(productList: List<ProductOrderModel>) {
        this.productList = productList.toMutableList()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val orderViewBinding: OrderViewBinding,val langId: String) :
        RecyclerView.ViewHolder(orderViewBinding.root) {
        fun bind(productHomeModel: ProductOrderModel,position:Int) {
            orderViewBinding.apply {
                productHome = productHomeModel
                productTitleText.text=productHomeModel.productTitle.asMap()[langId].toString()
            }
        }

    }

}


