package com.example.firebaseecom.detailsPg


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.AdCorousalViewBinding
import com.example.firebaseecom.databinding.ProductDetailsImageViewBinding

class ProductDetailsAdapter(private val context: Context):RecyclerView.Adapter<ProductDetailsAdapter.CarousalViewHolder>() {

    var imageList= emptyList<String?>()
    private lateinit var productDetailsImageViewBinding: ProductDetailsImageViewBinding

    fun setAd(imageList: List<String>)
    {
        this.imageList=imageList

        notifyDataSetChanged()
    }
    class CarousalViewHolder(productDetailsImageViewBinding: ProductDetailsImageViewBinding):RecyclerView.ViewHolder(productDetailsImageViewBinding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarousalViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        productDetailsImageViewBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.product_details_image_view,parent
            ,false)
        return CarousalViewHolder(productDetailsImageViewBinding)

    }

    override fun onBindViewHolder(holder: CarousalViewHolder, position: Int) {
        val imageUrl=imageList[position]
        Log.d("detailsImg",imageUrl.toString())
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.itemView.findViewById(R.id.adView))

    }

    override fun getItemCount(): Int {
        return imageList.count()
    }
}