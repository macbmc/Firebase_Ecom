package com.example.firebaseecom.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.OfferViewBinding
import com.example.firebaseecom.model.ProductHomeModel

class OfferZoneAdapter(private val fragmentFunctionClass: FragmentFunctionInterface) :
    RecyclerView.Adapter<OfferZoneAdapter.MyViewHolder>() {

    private lateinit var offerViewBinding: OfferViewBinding
    private var productList = mutableListOf<ProductHomeModel>()

    interface FragmentFunctionInterface {
        fun navToDetails(product: ProductHomeModel)
    }

    class MyViewHolder(val offerViewBinding: OfferViewBinding) :
        RecyclerView.ViewHolder(offerViewBinding.root) {
        fun bind(product: ProductHomeModel) {
            offerViewBinding.productHome = product
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        offerViewBinding = DataBindingUtil.inflate(inflater, R.layout.offer_view, parent, false)
        return MyViewHolder(offerViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
        Glide.with(holder.itemView)
            .load(product.productImage)
            .into(holder.offerViewBinding.productImage)
        holder.itemView.setOnClickListener {
            fragmentFunctionClass.navToDetails(product)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProducts(productList: List<ProductHomeModel>) {
        this.productList = productList.toMutableList()
        notifyDataSetChanged()
    }
}