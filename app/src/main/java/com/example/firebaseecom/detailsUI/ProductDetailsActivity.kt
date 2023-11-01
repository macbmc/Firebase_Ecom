@file:Suppress("DEPRECATION")

package com.example.firebaseecom.detailsUI

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductDetailsBinding
import com.example.firebaseecom.homeUI.CarousalAdapter
import com.example.firebaseecom.homeUI.HomeActivity
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.http.Url

@AndroidEntryPoint

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var activityProductDetailsBinding: ActivityProductDetailsBinding
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var productHome: ProductHomeModel
    private val carousalAdapter = CarousalAdapter(this@ProductDetailsActivity)
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
            productTitleText.text = productHome.productTitle
            productPriceText.text = productHome.productPrice.toString()
            backButton.setOnClickListener {
                finish()
            }
            buttonAddToCart.setOnClickListener {
                productDetailsViewModel.addToCart(productHome)
                Toast.makeText(this@ProductDetailsActivity, "Added to Cart", Toast.LENGTH_SHORT)
                    .show()
            }
            buttonBuyNow.setOnClickListener {
                productDetailsViewModel.addToOrders(productHome)
                Toast.makeText(this@ProductDetailsActivity, "Added to Orders", Toast.LENGTH_SHORT)
                    .show()
                val intent= Intent(this@ProductDetailsActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)

            }
            shareButton.setOnClickListener{
                productDetailsViewModel.shareProduct(productHome,this@ProductDetailsActivity)

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