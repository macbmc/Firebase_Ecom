@file:Suppress("DEPRECATION")

package com.example.firebaseecom.CartOrder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductOrderViewBinding
import com.example.firebaseecom.main.BaseActivity
import com.example.firebaseecom.model.ProductOrderModel
import com.example.firebaseecom.model.asMap
import com.example.firebaseecom.productLocation.TrackProductActivity
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
            titleText.text = productOrderModel?.productMultiLanguage!!.asMap()[langId].toString()
            Glide.with(this@ProductOrderViewActivity)
                .load(productOrderModel?.productImage)
                .error(R.drawable.placeholder_image)
                .into(productImage)
            orderedDate.text = getString(R.string.ordered_date, productOrderModel?.orderDate)
            deliverDate.text = getString(R.string.delivery_date, productOrderModel?.deliveryDate)
            navPop.setOnClickListener { finish() }
            trackButton.setOnClickListener {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    navToProductTrack()
                }
                else
                {
                    Toast.makeText(this@ProductOrderViewActivity,"Grant Location permission",Toast.LENGTH_LONG).show()
                }

            }
        }


    }

    private fun navToProductTrack() {
        val intent= Intent(
            this@ProductOrderViewActivity,
            TrackProductActivity::class.java
        )
        intent.putExtra("product", activityProductOrderViewBinding.productOrderModel as Serializable)
        startActivity(intent)
    }


}