package com.example.firebaseecom.detailsPg

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.SimilarProductLayoutBinding
import com.example.firebaseecom.model.ProductHomeModel

class SimilarProductsAdapter(val activityFunctionClass: ProductDetailsActivity.ActivityFunctionClass) :
    RecyclerView.Adapter<SimilarProductsAdapter.ClassViewHolder>() {

    private lateinit var similarProductLayoutBinding: SimilarProductLayoutBinding
    private var productList = mutableListOf<ProductHomeModel>()

    interface ActivityFunctionInterface {
        fun navToCategoryView(category: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        similarProductLayoutBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.similar_product_layout, parent, false)
        return ClassViewHolder(similarProductLayoutBinding)
    }

    class ClassViewHolder(private val similarProductLayoutBinding: SimilarProductLayoutBinding?) :
        RecyclerView.ViewHolder(similarProductLayoutBinding!!.root) {
        fun bind(productModel: ProductHomeModel) {
            similarProductLayoutBinding?.productDetails = productModel
        }

    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val productModel = productList[position]
        holder.bind(productModel)
        Glide.with(holder.itemView)
            .load(productModel.productImage)
            .error(R.drawable.ic_laptop)
            .into(similarProductLayoutBinding.productImg)
        holder.itemView.setOnClickListener {
            activityFunctionClass.navToCategoryView(productModel.productCategory!!)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setProducts(productList: List<ProductHomeModel>) {
        this.productList = productList.toMutableList()
        notifyItemChanged(productList.size - 1)
    }
}