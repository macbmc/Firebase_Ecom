package com.example.firebaseecom.productSearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityProductSearchBinding
import com.example.firebaseecom.detailsPg.ProductDetailsActivity
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class ProductSearchActivity : AppCompatActivity() {
    private lateinit var activityProductSearchBinding: ActivityProductSearchBinding
    private lateinit var productSearchVIewModel: ProductSearchVIewModel
    val adapter = ProductSearchAdapter(ProductSearchClass())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductSearchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_search)
        productSearchVIewModel = ViewModelProvider(this)[ProductSearchVIewModel::class.java]

        activityProductSearchBinding.apply {
            searchRecyclerView.adapter = adapter
            searchRecyclerView.layoutManager = LinearLayoutManager(
                this@ProductSearchActivity, LinearLayoutManager.VERTICAL,
                false
            )
            searchButton.setOnClickListener {
                if (searchView.query.toString().isNotEmpty()) {
                    observeProducts(searchView.query.toString())
                }
            }
            backButton.setOnClickListener {
                finish()
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    observeProducts(query)

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
    }

    private fun observeProducts(query: String?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
            productSearchVIewModel.searchProducts(query!!)
            productSearchVIewModel.product.collect {
                when (it) {
                    is Resource.Success -> {
                        activityProductSearchBinding.progressBar.isVisible = false
                        Log.d("queryRes", it.data.toString())
                        if (it.data.isNotEmpty()) {
                            activityProductSearchBinding.centerBanner.isVisible = false
                            adapter.setProducts(it.data)
                        } else {
                            activityProductSearchBinding.centerBanner.text =
                                getString(R.string.no_data)
                        }
                    }

                    is Resource.Loading -> {
                        activityProductSearchBinding.progressBar.isVisible = true
                    }

                    is Resource.Failed -> {
                        Toast.makeText(this@ProductSearchActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            }
        }
        }

    }

    inner class ProductSearchClass : ProductSearchAdapter.ProductCategoryInterface {
        override fun addToCart(productHomeModel: ProductHomeModel) {
           productSearchVIewModel.addToCart(productHomeModel)
        }

        override fun navToDetails(productHomeModel: ProductHomeModel) {
            val intent = Intent(this@ProductSearchActivity, ProductDetailsActivity::class.java)
            intent.putExtra("product", productHomeModel as Serializable)
            startActivity(intent)
        }

    }
}