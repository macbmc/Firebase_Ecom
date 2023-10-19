package com.example.firebaseecom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.firebaseecom.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding:ActivityHomeBinding
    private var demoProduct : List<ProductModel> = listOf(
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
        homeBinding=DataBindingUtil.setContentView(this,R.layout.activity_home)

        val adView=homeBinding.adView
        val carousalAdapter = CarousalAdapter(this@HomeActivity)
        adView.adapter=carousalAdapter
        carousalAdapter.setCarousal(imageList)


        val homeItemView=homeBinding.homeItemView
        val adapter = ProductListAdapter(this@HomeActivity)
        homeItemView.layoutManager=GridLayoutManager(this@HomeActivity,2)
        homeItemView.adapter = adapter
        adapter.setProduct(demoProduct)




    }
}
