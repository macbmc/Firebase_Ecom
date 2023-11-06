package com.example.firebaseecom.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductCategoryActivity : AppCompatActivity() {
    lateinit var activityProductListBinding: ActivityProductListBinding
    var adapter=ProductCategoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductListBinding=DataBindingUtil.setContentView(this,R.layout.activity_product_list)
        val category = intent.getStringExtra("category")
        activityProductListBinding.apply {
            destText.text=category
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(this@ProductCategoryActivity
                ,LinearLayoutManager.VERTICAL,false)


        }
    }
}