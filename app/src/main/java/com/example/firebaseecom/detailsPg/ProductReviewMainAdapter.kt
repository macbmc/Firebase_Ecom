package com.example.firebaseecom.detailsPg

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ReviewMainLayoutBinding
import com.example.firebaseecom.model.ProductOrderReviews
import com.example.firebaseecom.model.UserModel

class ProductReviewMainAdapter(val context: Context) :
    RecyclerView.Adapter<ProductReviewMainAdapter.DetailViewHolder>() {

    private var reviewList = mutableListOf<ProductOrderReviews>()
    private var reviewUserList = mutableListOf<UserModel>()
    private lateinit var productReviewLayoutBinding: ReviewMainLayoutBinding

    fun setReview(reviewList: List<ProductOrderReviews>, reviewUserList: List<UserModel>) {
        this.reviewUserList = reviewUserList.toMutableList()
        this.reviewList = reviewList.toMutableList()
        notifyDataSetChanged()
    }

    inner class DetailViewHolder(private val productReviewLayoutBinding: ReviewMainLayoutBinding) :
        RecyclerView.ViewHolder(productReviewLayoutBinding.root) {
        fun bind(productOrderReviews: ProductOrderReviews, userModel: UserModel) {
            productReviewLayoutBinding.apply {
                productReview = productOrderReviews
                reviewUser = userModel
                if (reviewUser?.userName!!.isEmpty())
                    userName.text = context.getString(R.string.anonymous)
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
            R.layout.review_main_layout, parent, false
        )
        return DetailViewHolder(productReviewLayoutBinding)

    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(reviewList[position], reviewUserList[position])
        Glide.with(holder.itemView)
            .load(reviewUserList[position].userImg)
            .error(R.drawable.placeholder_image)
            .into(holder.itemView.findViewById(R.id.userImage))

    }

    override fun getItemCount(): Int {
        return reviewList.size
    }
}