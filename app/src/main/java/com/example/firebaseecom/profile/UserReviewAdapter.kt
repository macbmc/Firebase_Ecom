package com.example.firebaseecom.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ReviewMainLayoutBinding
import com.example.firebaseecom.databinding.UserReviewLayoutBinding
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.ProductOrderReviews
import com.example.firebaseecom.model.UserModel

class UserReviewAdapter : RecyclerView.Adapter<UserReviewAdapter.DetailViewHolder>() {

    private var reviewList = mutableListOf<ProductOrderReviews>()
    private var productList = mutableListOf<ProductHomeModel>()
    private lateinit var productReviewLayoutBinding: UserReviewLayoutBinding

    fun setReview(reviewList: List<ProductOrderReviews>, productList: List<ProductHomeModel>) {
        this.productList = productList.toMutableList()
        this.reviewList = reviewList.toMutableList()
        notifyDataSetChanged()
    }

    inner class DetailViewHolder(private val productReviewLayoutBinding: UserReviewLayoutBinding) :
        RecyclerView.ViewHolder(productReviewLayoutBinding.root) {
        fun bind(productOrderReviews: ProductOrderReviews, productHomeModel: ProductHomeModel) {
            productReviewLayoutBinding.apply {
                product=productHomeModel
                review=productOrderReviews
                productRating.rating = review?.productRating!!.toFloat()

            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        productReviewLayoutBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.user_review_layout, parent, false
        )
        return DetailViewHolder(productReviewLayoutBinding)

    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(reviewList[position], productList[position])

        Glide.with(holder.itemView)
            .load(productList[position].productImage)
            .error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.productImage))

    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}