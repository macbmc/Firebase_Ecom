package com.example.firebaseecom.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductListBinding
import com.example.firebaseecom.detailsPg.ProductDetailsActivity
import com.example.firebaseecom.main.BaseActivity
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.utils.Resource
import com.example.firebaseecom.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class ProductCategoryActivity : BaseActivity() {


    private lateinit var activityProductListBinding: ActivityProductListBinding
    private lateinit var productCategoryViewModel: ProductCategoryViewModel
    private lateinit var adapter: ProductCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_list)
        productCategoryViewModel = ViewModelProvider(this)[ProductCategoryViewModel::class.java]
        val category = intent.getStringExtra("category")
        adapter = ProductCategoryAdapter(ProductCategoryClass(), langId)
        observeProducts(category!!)
        activityProductListBinding.apply {

            ButtonHolder.visibility = View.GONE
            destText.text = category
            backButton.setOnClickListener {
                finish()
            }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(
                this@ProductCategoryActivity, LinearLayoutManager.VERTICAL, false
            )


        }
    }

    private fun observeProducts(category: String) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productCategoryViewModel.getProductsByCat(category)
                productCategoryViewModel.products.collect {
                    when (it) {
                        is Resource.Loading -> {
                            activityProductListBinding.progressBar.isVisible = true
                        }

                        is Resource.Success -> {
                            activityProductListBinding.progressBar.isVisible = false
                            adapter.setProducts(it.data)
                        }

                        is Resource.Failed -> {
                            activityProductListBinding.progressBar.isVisible = true
                            ToastUtils().giveToast(it.message, this@ProductCategoryActivity)
                        }
                    }
                }
            }
        }

    }

    inner class ProductCategoryClass : ProductCategoryAdapter.ProductCategoryInterface {
        override fun addToCart(productHomeModel: ProductHomeModel) {
            productCategoryViewModel.addToCart(productHomeModel)
            Toast.makeText(
                this@ProductCategoryActivity, "Added to Cart",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun navToDetails(productHomeModel: ProductHomeModel) {
            val intent = Intent(this@ProductCategoryActivity, ProductDetailsActivity::class.java)
            intent.putExtra("product", productHomeModel as Serializable)
            startActivity(intent)
        }

    }
}