@file:Suppress("DEPRECATION")

package com.example.firebaseecom.detailsUI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductDetailsBinding
import com.example.firebaseecom.model.ProductModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var productDetailsBinding: ActivityProductDetailsBinding
    private lateinit var productModel: ProductModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_product_details
        )
        productModel = intent.extras!!.get("product") as ProductModel
        productDetailsBinding.productDetails=productModel
    }
}