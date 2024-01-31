package com.example.firebaseecom.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityUserReviewBinding
import com.example.firebaseecom.main.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserReviewActivity : BaseActivity() {

    private lateinit var activityUserReviewBinding: ActivityUserReviewBinding
    private lateinit var userReviewViewModel: UserReviewViewModel
    val adapter = UserReviewAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUserReviewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_user_review)
        userReviewViewModel = ViewModelProvider(this)[UserReviewViewModel::class.java]
        activityUserReviewBinding.navPop.setOnClickListener {
            finish()
        }

        observeUserReview()
    }

    private fun observeUserReview() {
        activityUserReviewBinding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED)
                {
                    userReviewViewModel.apply {
                        getProductReviewInfo()
                        reviewInfo.observe(this@UserReviewActivity) { reviewInfo ->
                            appPoints.text = getString(
                                R.string.you_have_done_7_ekart_reviews,
                                reviewInfo.size.toString()
                            )
                            productInfo.observe(this@UserReviewActivity) { productInfo ->
                                reviewView.adapter = adapter
                                reviewView.layoutManager = LinearLayoutManager(
                                    this@UserReviewActivity,
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                progressBar.visibility = View.GONE
                                adapter.setReview(reviewInfo, productInfo)
                                Log.d("ReviewAd", productInfo.toString())
                                Log.d("ReviewAb", reviewInfo.toString())


                            }
                        }
                    }

                }
            }
        }
    }
}