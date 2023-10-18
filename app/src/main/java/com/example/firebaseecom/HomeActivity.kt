package com.example.firebaseecom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2


class HomeActivity : AppCompatActivity(R.layout.activity_home) {
    val imageList:List<Int> = listOf(R.drawable.placeholder_image,R.drawable.placeholder_image)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adView=findViewById<ViewPager2>(R.id.adView)
        adView.adapter=CarousalAdapter(this@HomeActivity)


    }
}
