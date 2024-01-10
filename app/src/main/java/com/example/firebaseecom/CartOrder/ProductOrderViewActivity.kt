@file:Suppress("DEPRECATION")

package com.example.firebaseecom.CartOrder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.api.ProductOrderDeliveryStatus
import com.example.firebaseecom.databinding.ActivityProductOrderViewBinding
import com.example.firebaseecom.main.BaseActivity
import com.example.firebaseecom.model.ProductOrderModel
import com.example.firebaseecom.model.asMap
import com.example.firebaseecom.productLocation.TrackProductActivity
import com.example.firebaseecom.utils.AlertDialogUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class ProductOrderViewActivity : BaseActivity() {
    private lateinit var activityProductOrderViewBinding: ActivityProductOrderViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
        activityProductOrderViewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_order_view)
        activityProductOrderViewBinding.apply {
            productOrderModel = intent.extras!!.get("productDetails") as ProductOrderModel
            getOrderStatus(productOrderModel!!)
            getButtonForReview(productOrderModel!!)
            navPop.setOnClickListener { finish() }
            trackButton.setOnClickListener {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    navToProductTrack()
                } else {
                    Toast.makeText(
                        this@ProductOrderViewActivity,
                        getString(R.string.grant_location_permission),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            reviewButton.setOnClickListener {
                giveProductReview(productOrderModel!!)
            }

        }


    }

    private fun giveProductReview(productOrderModel: ProductOrderModel) {
        val orderViewModel = ViewModelProvider(this)[ProductOrderViewModel::class.java]
        val alertDialogUtils = AlertDialogUtils()
        alertDialogUtils.showReviewAlertDialog(this, productOrderModel)
        alertDialogUtils.productReview.observe(this@ProductOrderViewActivity) { review ->
            Log.d("ProductReview", review.toString())
            orderViewModel.addToReviews(review)
            activityProductOrderViewBinding.reviewButton.visibility = View.GONE

        }


    }


    private fun getButtonForReview(productOrderModel: ProductOrderModel) {
        activityProductOrderViewBinding.apply {
            if (ProductOrderDeliveryStatus.ORDER_DELIVERED.statusCode == productOrderModel.productDeliveryStatusCode) {
                trackButton.visibility = View.GONE
                reviewButton.visibility = View.VISIBLE
            }
        }

    }

    private fun getOrderStatus(productOrderModel: ProductOrderModel) {
        activityProductOrderViewBinding.apply {
            titleText.text = productOrderModel?.productMultiLanguage!!.asMap()[langId].toString()
            Glide.with(this@ProductOrderViewActivity).load(productOrderModel?.productImage)
                .error(R.drawable.placeholder_image).into(productImage)
            orderedDate.text = getString(R.string.ordered_date, productOrderModel?.orderDate)
            deliverDate.text = getString(R.string.delivery_date, productOrderModel?.deliveryDate)

            orderStatus.text = ProductOrderDeliveryStatus.values().find {
                it.statusCode == productOrderModel?.productDeliveryStatusCode
            }?.msg
        }

    }

    private fun navToProductTrack() {
        val intent = Intent(
            this@ProductOrderViewActivity, TrackProductActivity::class.java
        )
        intent.putExtra(
            "product", activityProductOrderViewBinding.productOrderModel as Serializable
        )
        startActivity(intent)
    }


}