package com.example.firebaseecom.detailsPg

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ReviewLayoutBinding
import com.example.firebaseecom.model.ProductOrderReviews
import kotlin.math.min

class ProductReviewAdapter : RecyclerView.Adapter<ProductReviewAdapter.DetailViewHolder>() {

    private var reviewList = mutableListOf<ProductOrderReviews>()
    private lateinit var productReviewLayoutBinding: ReviewLayoutBinding

    fun setReview(reviewList: List<ProductOrderReviews>) {

        this.reviewList = reviewList.toMutableList()
        notifyDataSetChanged()
    }

    class DetailViewHolder(private val productReviewLayoutBinding: ReviewLayoutBinding) :
        RecyclerView.ViewHolder(productReviewLayoutBinding.root) {
        fun bind(productOrderReviews: ProductOrderReviews) {
            productReviewLayoutBinding.apply {
                textReview.text = productOrderReviews.productReview
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
            R.layout.review_layout, parent, false
        )
        return DetailViewHolder(productReviewLayoutBinding)

    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(reviewList[position])

    }

    override fun getItemCount(): Int {
        return min(3, reviewList.size)
    }
}