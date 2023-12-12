package com.example.firebaseecom.detailsPg


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ProductDetailsImageViewBinding

class ProductDetailsAdapter : RecyclerView.Adapter<ProductDetailsAdapter.DetailViewHolder>() {

    private var imageList = emptyList<String?>()
    private lateinit var productDetailsImageViewBinding: ProductDetailsImageViewBinding

    fun setProduct(imageList: List<String>) {
        this.imageList = imageList

        notifyDataSetChanged()
    }

    class DetailViewHolder(productDetailsImageViewBinding: ProductDetailsImageViewBinding) :
        RecyclerView.ViewHolder(productDetailsImageViewBinding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        productDetailsImageViewBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.product_details_image_view, parent, false
        )
        return DetailViewHolder(productDetailsImageViewBinding)

    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val imageUrl = imageList[position]
        Log.d("detailsImg", imageUrl.toString())
        Glide.with(holder.itemView)
            .load(imageUrl)
            .thumbnail(Glide.with(holder.itemView).load(R.drawable.preloader))
            .error(R.drawable.error_image)
            .into(holder.itemView.findViewById(R.id.adView))

    }

    override fun getItemCount(): Int {
        return imageList.count()
    }
}