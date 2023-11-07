package com.example.firebaseecom.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.CartOrder.ProductListActivity
import com.example.firebaseecom.productSearch.ProductSearchActivity
import com.example.firebaseecom.R
import com.example.firebaseecom.api.EkartApiEndPoints
import com.example.firebaseecom.category.ProductCategoryActivity
import com.example.firebaseecom.databinding.ActivityHomeBinding
import com.example.firebaseecom.detailsPg.ProductDetailsActivity
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.profile.UserProfileActivity
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint


class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private val carousalAdapter = CarousalAdapter(this@HomeActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val adView = homeBinding.carousalView

        adView.adapter = carousalAdapter
        adView.layoutManager = LinearLayoutManager(
            this@HomeActivity, LinearLayoutManager.HORIZONTAL,
            false
        )

        observeCartNumber()
        observeCarousal()
        observeProducts()



        homeBinding.apply {
            cartNumber.text="0"
            profButton.setOnClickListener {
                startActivity(Intent(this@HomeActivity, UserProfileActivity::class.java))
            }
            searchHomeButton.setOnClickListener {
                startActivity(Intent(this@HomeActivity, ProductSearchActivity::class.java))
            }
            cartHomeButton.setOnClickListener{
                val intent =Intent(this@HomeActivity,ProductListActivity::class.java)
                intent.putExtra("dest","cart")
                startActivity(intent)
            }
            catLaptop.setOnClickListener{
                val intent=Intent(this@HomeActivity,ProductCategoryActivity::class.java)
                intent.putExtra("category","Laptop")
                startActivity(intent)
            }
            catPhones.setOnClickListener{
                val intent=Intent(this@HomeActivity,ProductCategoryActivity::class.java)
                intent.putExtra("category","Phone")
                startActivity(intent)
            }
            catTablet.setOnClickListener{
                val intent=Intent(this@HomeActivity,ProductCategoryActivity::class.java)
                intent.putExtra("category","Tablet")
                startActivity(intent)
            }
        }


    }

    override fun onResume() {
        super.onResume()

        observeCartNumber()
    }

    private fun observeCartNumber() {
        lifecycleScope.launch {
            val size = homeViewModel.checkNumbWishlist("cart")
            homeBinding.cartNumber.text=size.toString()

        }
    }


    private fun observeCarousal() {

        lifecycleScope.launch {
           homeViewModel.getAd()
            homeViewModel.adList.collect{
                when(it){
                    is Resource.Loading ->{
                        homeBinding.homeadViewProgress.isVisible=true
                    }
                    is Resource.Success ->{
                        homeBinding.homeadViewProgress.isVisible=false
                        carousalAdapter.setAd(it.data)
                    }
                    else -> {}
                }
            }
        }

    }

    private fun observeProducts() {
        val homeItemView = homeBinding.homeItemView
        val adapter = ProductHomeAdapter(NavigateClass())
        homeItemView.layoutManager = GridLayoutManager(this@HomeActivity, 2)
        homeItemView.adapter = adapter
        homeBinding.apply {
            lifecycleScope.launch {
                homeViewModel.getProductHome()
                homeViewModel.products.collect()
                {
                    when (it) {
                        is Resource.Loading -> {
                            Log.d("base",EkartApiEndPoints.END_POINT_BASE.ordinal.toString())
                            Log.d("base",EkartApiEndPoints.END_POINT_PRODUCT_META.toString())
                            Log.d("base",EkartApiEndPoints.END_POINT_PRODUCTS.toString())
                            homeItemViewProgress.visibility = View.VISIBLE
                            Log.d("itemViewLoader", "Loading")
                        }

                        is Resource.Success -> {
                            homeItemViewProgress.visibility = View.INVISIBLE
                            Log.d("itemViewLoader", "success")
                            adapter.setProduct(it.data)

                        }

                        is Resource.Failed -> {
                            homeItemViewProgress.visibility = View.VISIBLE
                            Log.d("itemViewLoader", "Failed")
                            Toast.makeText(this@HomeActivity, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }
            }
        }

    }

   inner class NavigateClass:ProductHomeAdapter.NavigationInterface{
       override fun navigateToDetails(productModel: ProductHomeModel) {
           val intent = Intent(this@HomeActivity, ProductDetailsActivity::class.java)
           intent.putExtra("product", productModel as Serializable)
           startActivity(intent)
       }

   }
}
