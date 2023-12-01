@file:Suppress("DEPRECATION")

package com.example.firebaseecom.detailsPg

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductDetailsBinding
import com.example.firebaseecom.main.BaseActivity
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.ProductMultiLanguage
import com.example.firebaseecom.model.asMap
import com.example.firebaseecom.payments.ProductCheckoutActivity
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class ProductDetailsActivity : BaseActivity() {
    private lateinit var activityProductDetailsBinding: ActivityProductDetailsBinding
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var productHome: ProductHomeModel
    private val carousalAdapter = ProductDetailsAdapter()
    private val snapHelper = LinearSnapHelper()
    var productList = arrayListOf<ProductHomeModel?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailsViewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        activityProductDetailsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        productHome = intent.extras!!.get("product") as ProductHomeModel
        val productView = activityProductDetailsBinding.productCarousalView
        snapHelper.attachToRecyclerView(productView)
        productView.adapter = carousalAdapter
        productView.layoutManager = LinearLayoutManager(
            this@ProductDetailsActivity, LinearLayoutManager.HORIZONTAL, false
        )
        val productTitleMap = getLanguageMap(productHome.productTitle)

        observeProductDetails()

        activityProductDetailsBinding.apply {
            productTitleText.text = productTitleMap[langId].toString()
            productTitleHeader.text = productTitleMap[langId].toString()
            productPriceText.text = productHome.productPrice.toString()
            shareButton.setOnClickListener {
                productDetailsViewModel.shareProduct(productHome, this@ProductDetailsActivity)
            }
            backButton.setOnClickListener {
                finish()
            }
            buttonBuyNow.setOnClickListener {
                productList.add(productHome)
                Log.d("productList", productList.toString())
                val intent =
                    Intent(this@ProductDetailsActivity, ProductCheckoutActivity::class.java)
                if (productList.isNotEmpty()) {
                    intent.putExtra("productList", productList)
                }
                startActivity(intent)
            }
            buttonAddToCart.setOnClickListener {
                productDetailsViewModel.addToCart(productHome)
                Toast.makeText(this@ProductDetailsActivity, "Added To Cart", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }

    private fun getLanguageMap(productMultiLanguage: ProductMultiLanguage): Map<String, Any?> {
        return productMultiLanguage.asMap()
    }

    private fun observeProductDetails() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productDetailsViewModel.getProductDetails(productHome.productId!!)
                productDetailsViewModel.productDetails.collect {
                    when (it) {
                        is Resource.Loading -> {
                            activityProductDetailsBinding.progressBar.isVisible = true
                            Toast.makeText(
                                this@ProductDetailsActivity,
                                getString(R.string.details_loading),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Resource.Success -> {
                            activityProductDetailsBinding.apply {
                                progressBar.isVisible = false
                                val myList = it.data
                                productDetails = myList?.singleOrNull { list ->
                                    list.productId == productHome.productId
                                }

                                productReviewText.text =
                                    getLanguageMap(productDetails!!.productReviews)[langId].toString()
                                productDescText.text =
                                    getLanguageMap(productDetails!!.productDescription)[langId].toString()

                                carousalAdapter.setProduct(productDetails?.productImage!!)
                            }
                        }

                        is Resource.Failed -> {
                            activityProductDetailsBinding.progressBar.isVisible = true
                            Toast.makeText(
                                this@ProductDetailsActivity, it.message, Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}