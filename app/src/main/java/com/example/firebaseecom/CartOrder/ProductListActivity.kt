package com.example.firebaseecom.CartOrder

import ProductListAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.firebaseecom.databinding.ActivityProductListBinding
import com.example.firebaseecom.detailsPg.ProductDetailsActivity
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class ProductListActivity : AppCompatActivity() {
    private lateinit var activityProductListBinding: ActivityProductListBinding
    private lateinit var productListViewModel: ProductListViewModel
    val adapter = ProductListAdapter(ActivityFunctionClass())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_list)
        productListViewModel = ViewModelProvider(this)[ProductListViewModel::class.java]
        val dest = intent.getStringExtra("dest")
        activityProductListBinding.apply {
            backButton.setOnClickListener {
                finish()
            }
            destText.text = dest
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(
                this@ProductListActivity, LinearLayoutManager.VERTICAL,
                false
            )
        }
        observeProducts(dest)
    }

    private fun observeProducts(dest: String?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productListViewModel.getProductFromDest(dest!!)
                productListViewModel.productList.collect {
                    when (it) {
                        is Resource.Loading -> {
                            activityProductListBinding.progressBar.isVisible = true
                        }

                        is Resource.Success -> {
                            activityProductListBinding.progressBar.isVisible = false
                            Log.d("cartData", it.data.toString())
                            adapter.setProduct(it.data)

                        }

                        is Resource.Failed -> {
                            activityProductListBinding.progressBar.isVisible = false
                            Toast.makeText(this@ProductListActivity, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }

        }


    }

    inner class ActivityFunctionClass : ProductListAdapter.ActivityFunctionInterface {
        override fun navigateToDetails(productHomeModel: ProductHomeModel) {
            val intent = Intent(this@ProductListActivity, ProductDetailsActivity::class.java)
            intent.putExtra("product", productHomeModel as Serializable)
            startActivity(intent)
        }

        override fun deleteFromCart(productHomeModel: ProductHomeModel) {

            productListViewModel.removeFromCart(productHomeModel)


        }
    }
}