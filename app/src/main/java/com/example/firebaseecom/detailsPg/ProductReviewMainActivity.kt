package com.example.firebaseecom.detailsPg

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductReviewMainBinding
import com.example.firebaseecom.main.BaseActivity
import com.example.firebaseecom.model.ProductOrderReviews
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@Suppress("DEPRECATION")
class ProductReviewMainActivity : BaseActivity() {
    private lateinit var productReviewMainBinding: ActivityProductReviewMainBinding
    private lateinit var reviewViewModel: ProductReviewViewModel
    val adapter = ProductReviewMainAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productReviewMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_review_main)
        val review = intent.extras?.getSerializable("reviews") as List<ProductOrderReviews>
        reviewViewModel = ViewModelProvider(this)[ProductReviewViewModel::class.java]
        productReviewMainBinding.navPop.setOnClickListener {
            finish()
        }

        observeReviewUsers(review)
    }

    private fun observeReviewUsers(review: List<ProductOrderReviews>) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                reviewViewModel.getReviewUser(review)
                reviewViewModel.userDetails.observe(this@ProductReviewMainActivity) { reviewUserList ->
                    Log.d("reviewUser", reviewUserList.toString())
                    productReviewMainBinding.apply {
                        reviewLayout.adapter = adapter
                        reviewLayout.layoutManager = LinearLayoutManager(
                            this@ProductReviewMainActivity,
                            LinearLayoutManager.VERTICAL, false
                        )
                        adapter.setReview(review, reviewUserList)
                        reviewProgress.isVisible = false
                    }

                }
            }
        }


    }
}