@file:Suppress("DEPRECATION")

package com.example.firebaseecom.detailsUI

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductDetailsBinding
import com.example.firebaseecom.homeUI.CarousalAdapter
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var activityProductDetailsBinding: ActivityProductDetailsBinding
    lateinit var productDetailsViewModel: ProductDetailsViewModel
    lateinit var productHome: ProductHomeModel
    val carousalAdapter = CarousalAdapter(this@ProductDetailsActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailsViewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        activityProductDetailsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        productHome = intent.extras!!.get("product") as ProductHomeModel
        val adView = activityProductDetailsBinding.productCarousalView

        adView.adapter = carousalAdapter
        adView.layoutManager = LinearLayoutManager(
            this@ProductDetailsActivity, LinearLayoutManager.HORIZONTAL,
            false
        )

        observeProductDetails()

        activityProductDetailsBinding.apply {
            productTitleText.setText(productHome.productTitle)
            productPriceText.setText(productHome.productPrice.toString())
            backButton.setOnClickListener{
                finish()
            }

        }

    }

    private fun observeProductDetails() {
        lifecycleScope.launch {
            productDetailsViewModel.getProductDetails(productHome.productId!!)
            productDetailsViewModel.productDetails.collect {
                when (it) {
                    is Resource.Loading -> {
                        Toast.makeText(
                            this@ProductDetailsActivity, "Details Loading", Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Success -> {
                        val myList = it.data
                        activityProductDetailsBinding.productDetails = myList?.singleOrNull {
                            it.productId == productHome.productId
                        }
                        carousalAdapter.setAd(activityProductDetailsBinding.productDetails?.productImage!!)
                    }

                    else -> {}
                }
            }
        }
    }
}