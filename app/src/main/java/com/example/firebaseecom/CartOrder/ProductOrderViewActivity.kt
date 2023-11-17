@file:Suppress("DEPRECATION")

package com.example.firebaseecom.CartOrder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductOrderViewBinding
import com.example.firebaseecom.model.ProductOrderModel

class ProductOrderViewActivity : AppCompatActivity() {
    private lateinit var activityProductOrderViewBinding: ActivityProductOrderViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductOrderViewBinding=DataBindingUtil.setContentView(this,R.layout.activity_product_order_view)
        activityProductOrderViewBinding.apply{productOrderModel=intent.extras!!.get("productDetails") as ProductOrderModel
        Glide.with(this@ProductOrderViewActivity)
            .load(productOrderModel?.productImage)
            .error(R.drawable.placeholder_image)
            .into(productImage)
        orderedDate.text=getString(R.string.ordered_date,productOrderModel?.orderDate)
        deliverDate.text=getString(R.string.delivery_date,productOrderModel?.deliveryDate)
        navPop.setOnClickListener{finish()}
        }
    }
}