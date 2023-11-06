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
import com.example.firebaseecom.ProductSearchActivity
import com.example.firebaseecom.R
import com.example.firebaseecom.api.EkartApiEndPoints
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
    lateinit var homeViewModel: HomeViewModel
    val carousalAdapter = CarousalAdapter(this@HomeActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val adView = homeBinding.carousalView

        adView.adapter = carousalAdapter
        adView.layoutManager = LinearLayoutManager(
            this@HomeActivity, LinearLayoutManager.HORIZONTAL,
            false
        )


        observeCarousal()
        observeProducts()
        ObserveCartNumber()


        homeBinding.apply {
            cartNumber.text = "0"
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
        }


    }

    override fun onResume() {
        super.onResume()
        ObserveCartNumber()
    }

    private fun ObserveCartNumber() {
        lifecycleScope.launch {
            var size = homeViewModel.checkNumbWishlist("cart")
            Log.d("deferredcartnumber", size.toString())
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
        val adapter = ProductHomeAdapter(this@HomeActivity,NavigateClass())
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
                            Log.d("itemviewLoader", "success")
                            adapter.setProduct(it.data)

                        }

                        is Resource.Failed -> {
                            homeItemViewProgress.visibility = View.VISIBLE
                            Log.d("itemViewLoader", "Failed")
                            Toast.makeText(this@HomeActivity, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {}
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
