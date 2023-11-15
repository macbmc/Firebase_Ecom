@file:Suppress("DEPRECATION")

package com.example.firebaseecom.payments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductCheckoutBinding
import com.example.firebaseecom.model.ProductHomeModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ProductCheckoutActivity : AppCompatActivity() {
    private lateinit var activityProductCheckoutBinding: ActivityProductCheckoutBinding
    private var productList = arrayListOf<ProductHomeModel>()
    val adapter = ProductCheckoutAdapter(ActivityFunctionClass())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductCheckoutBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_checkout)
        productList = (intent.extras?.get("productList") as? ArrayList<ProductHomeModel>)!!

        activityProductCheckoutBinding.apply {
            productListView.layoutManager = LinearLayoutManager(
                this@ProductCheckoutActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            productListView.adapter = adapter
            backButton.setOnClickListener {
                finish()
            }
        }
        adapter.setProducts(productList.toList())
    }

    inner class ActivityFunctionClass : ProductCheckoutAdapter.ActivityFunctionInterface {
        override fun addTotalPrice(productList:List<ProductHomeModel?>) {
            var totalPrice = 0
            for (product in productList) {
                totalPrice += product?.productPrice!!
            }
            activityProductCheckoutBinding.totalPrice.text =
                getString(R.string.total_amount_to_be_paid) + totalPrice.toString()

        }
    }
}
