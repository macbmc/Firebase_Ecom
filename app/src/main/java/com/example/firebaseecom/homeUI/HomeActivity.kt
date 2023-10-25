package com.example.firebaseecom.homeUI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.authUI.AuthViewModel
import com.example.firebaseecom.authUI.SignUpActivity
import com.example.firebaseecom.databinding.ActivityHomeBinding
import com.example.firebaseecom.model.ProductModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint


class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding:ActivityHomeBinding
    private var demoProduct : List<ProductModel> = listOf(
        ProductModel(
            "Product1",
            "123",
            "R.drawable.placeholder_image.jpg",
            "45",
            "Hellooooo",
            productReview = listOf(ProductModel.Review("user1","content1")),

        ),
        ProductModel(
            "Product1",
            "123",
            "R.drawable.placeholder_image.jpg",
            "45",
            "Hellooooo",
            productReview = listOf(ProductModel.Review("user1","content1"))

        ),
        ProductModel(
            "Product1",
            "123",
            "R.drawable.placeholder_image.jpg",
            "45",
            "Hellooooo",
            productReview = listOf(ProductModel.Review("user1","content1"))

        )

    )
    var imageList:List<String> = listOf("R.drawable.placeholder_image","R.drawable.placeholder_image")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding=DataBindingUtil.setContentView(this, R.layout.activity_home)
        val adView = homeBinding.carousalView
        val carousalAdapter = CarousalAdapter(this@HomeActivity)
        adView.adapter=carousalAdapter
        adView.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,
            false)
        carousalAdapter.setAd(imageList)




        val homeItemView=homeBinding.homeItemView
        val adapter = ProductListAdapter(this@HomeActivity)
        homeItemView.layoutManager=GridLayoutManager(this@HomeActivity,2)
        homeItemView.adapter = adapter
        adapter.setProduct(demoProduct)

        homeBinding.profButton.setOnClickListener(){
            val authViewModel= ViewModelProvider(this@HomeActivity).get(AuthViewModel::class.java)
            authViewModel.logout()
            startActivity(Intent(this@HomeActivity,SignUpActivity::class.java))
        }



    }
}
