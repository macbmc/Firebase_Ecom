@file:Suppress("DEPRECATION")

package com.example.firebaseecom.detailsPg

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
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
import com.example.firebaseecom.model.ProductOffersModel
import com.example.firebaseecom.model.ProductOrderReviews
import com.example.firebaseecom.model.asMap
import com.example.firebaseecom.payments.ProductCheckoutActivity
import com.example.firebaseecom.utils.Resource
import com.example.firebaseecom.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint

class ProductDetailsActivity : BaseActivity() {
    private lateinit var activityProductDetailsBinding: ActivityProductDetailsBinding
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var productHome: ProductHomeModel
    private lateinit var selectedOffer: ProductOffersModel
    private lateinit var offers: List<ProductOffersModel>
    private val carousalAdapter = ProductDetailsAdapter()
    private val reviewAdapter = ProductReviewAdapter()
    private val snapHelper = LinearSnapHelper()
    var productList = arrayListOf<ProductHomeModel?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailsViewModel = ViewModelProvider(this)[ProductDetailsViewModel::class.java]
        activityProductDetailsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_details)
        productHome = intent.extras!!.get("product") as ProductHomeModel
        val bundle = intent.extras
        offers = if (bundle?.getSerializable("offers") != null) {
            bundle?.getSerializable("offers") as List<ProductOffersModel>
        } else
            listOf()
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

                productDetailsViewModel.shareProduct(productHome)
            }
            backButton.setOnClickListener {
                finish()
            }
            couponButton.setOnClickListener {
                acceptCoupon()
            }
            viewCoupons.setOnClickListener {
                showAvailableCoupons()
            }
            buttonBuyNow.setOnClickListener {
                addToBuy()

            }
            buttonAddToCart.setOnClickListener {
                productDetailsViewModel.addToCart(productHome)
                ToastUtils().giveToast(
                    getString(R.string.added_to_cart), this@ProductDetailsActivity
                )
            }


        }

    }

    private fun addToBuy() {
        var offerDesc = ""
        var offerAmnt = 0
        if (::selectedOffer.isInitialized) {
            when (activityProductDetailsBinding.editTextCoupon.text.toString()) {
                selectedOffer.couponDiscount -> {
                    offerDesc = selectedOffer.couponDiscount.toString()
                    offerAmnt = selectedOffer.productDiscount!!.toInt()
                    val discount =
                        productHome.productPrice?.times(
                            selectedOffer.productDiscount?.toDouble()!!.div(100)
                        )
                    productHome.productPrice = productHome.productPrice?.minus(discount!!.toInt())
                }
            }
        }

        productList.add(productHome)
        Log.d("productList", productList.toString())
        val intent =
            Intent(this@ProductDetailsActivity, ProductCheckoutActivity::class.java)
        if (productList.isNotEmpty()) {
            intent.putExtra("productList", productList)
            intent.putExtra("offerDesc", offerDesc)
            intent.putExtra("offerAmnt", offerAmnt)
            intent.putExtra(
                "productMrp",
                activityProductDetailsBinding.productPriceText.text.toString()
            )
        }
        startActivity(intent)
    }


    private fun acceptCoupon() {
        activityProductDetailsBinding.apply {
            if (editTextCoupon.text.isEmpty()) ToastUtils().giveToast(
                getString(R.string.select_a_coupon_first), this@ProductDetailsActivity
            )
            else {
                if (editTextCoupon.text.toString() == selectedOffer.couponDiscount || editTextCoupon.text.toString() == selectedOffer.couponVouchers)
                    ToastUtils().giveToast(
                        getString(R.string.coupon_applied),
                        this@ProductDetailsActivity
                    )
                else
                    ToastUtils().giveToast(
                        getString(R.string.select_a_coupon_first),
                        this@ProductDetailsActivity
                    )

            }
        }
    }

    private fun showAvailableCoupons() {
        activityProductDetailsBinding.apply {
            Log.d("offerList", offers.toString())
            for (offer in offers) {
                if (offer.productId == productHome.productId) {
                    selectedOffer = offer
                    couponDiscountHead.text = offer.couponDiscount
                    couponVoucherHead.text = offer.couponVouchers
                    couponDiscountDesc.text = offer.couponDiscountDesc
                    couponVoucherDesc.text = offer.couponVouchersDesc
                    couponList.isVisible = true
                    couponDiscountButton.setOnClickListener {
                        editTextCoupon.text = offer.couponDiscount?.toEditable()
                    }
                    couponVoucherButton.setOnClickListener {
                        editTextCoupon.text = offer.couponVouchers?.toEditable()
                    }

                }

            }
            if (!::selectedOffer.isInitialized)
                ToastUtils().giveToast(
                    getString(R.string.no_available_coupons),
                    this@ProductDetailsActivity
                )
            else {
                Log.d("smoothScroll", "called")
                layoutScrollView.post { layoutScrollView.smoothScrollTo(0, couponList.bottom) }
            }
        }

    }

    private fun getLanguageMap(productMultiLanguage: ProductMultiLanguage): Map<String, Any?> {
        return productMultiLanguage.asMap()
    }

    private fun observeProductDetails() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productDetailsViewModel.getProductReview(productHome.productId!!)
                productDetailsViewModel.getProductDetails(productHome.productId!!)
                productDetailsViewModel.productDetails.collect {
                    when (it) {
                        is Resource.Loading -> {
                            activityProductDetailsBinding.progressBar.isVisible = true
                        }

                        is Resource.Success -> {
                            activityProductDetailsBinding.apply {
                                progressBar.isVisible = false
                                val myList = it.data
                                productDetails = myList?.singleOrNull { list ->
                                    list.productId == productHome.productId
                                }


                                productDescText.text =
                                    getLanguageMap(productDetails!!.productDescription)[langId].toString()

                                carousalAdapter.setProduct(productDetails?.productImage!!)
                                productDetailsViewModel.reviewDetails.observe(this@ProductDetailsActivity) {reviewData->
                                    Log.d("reviewAct",reviewData.toString())
                                    if(reviewData.isNotEmpty())
                                    {
                                        reviewLayout.isVisible=true
                                        reviewView.adapter=reviewAdapter

                                        reviewView.layoutManager=LinearLayoutManager(this@ProductDetailsActivity,
                                            LinearLayoutManager.VERTICAL,false)
                                        reviewAdapter.setReview(reviewData)
                                        viewMoreReview.setOnClickListener{
                                            navToReview(reviewData)
                                        }
                                    }

                                }

                            }
                        }

                        is Resource.Failed -> {
                            activityProductDetailsBinding.progressBar.isVisible = true
                            ToastUtils().giveToast(it.message, this@ProductDetailsActivity)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun navToReview(reviewData: List<ProductOrderReviews>?) {
        val intent = Intent(this@ProductDetailsActivity,ProductReviewMainActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("reviews", reviewData as Serializable)
        intent.putExtras(bundle)
        startActivity(intent)

    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onResume() {
        super.onResume()
        productList = arrayListOf()
        activityProductDetailsBinding.editTextCoupon.text = null
        productHome = intent.extras!!.get("product") as ProductHomeModel

    }

}