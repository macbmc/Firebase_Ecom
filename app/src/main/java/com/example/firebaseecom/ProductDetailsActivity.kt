package com.example.firebaseecom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.firebaseecom.databinding.ActivityProductDetailsBinding

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var productDetailsBinding:ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailsBinding=DataBindingUtil.setContentView(this,R.layout.activity_product_details)
    }
}