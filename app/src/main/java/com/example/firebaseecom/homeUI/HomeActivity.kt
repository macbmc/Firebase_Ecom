package com.example.firebaseecom.homeUI

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
import com.example.firebaseecom.ProductSearchActivity
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityHomeBinding
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.profileUI.UserProfileActivity
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint


class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityHomeBinding
    lateinit var homeViewModel: HomeViewModel
    val carousalAdapter = CarousalAdapter(this@HomeActivity)

    var imageList: List<String> =
        listOf("R.drawable.placeholder_image", "R.drawable.placeholder_image")

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


        homeBinding.apply {
            profButton.setOnClickListener {
                startActivity(Intent(this@HomeActivity, UserProfileActivity::class.java))
            }
            searchHomeButton.setOnClickListener {
                startActivity(Intent(this@HomeActivity, ProductSearchActivity::class.java))
            }
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
        val adapter = ProductListAdapter(this@HomeActivity)
        homeItemView.layoutManager = GridLayoutManager(this@HomeActivity, 2)
        homeItemView.adapter = adapter
        homeBinding.apply {
            lifecycleScope.launch {
                homeViewModel.getProductHome()
                homeViewModel.products.collect()
                {
                    when (it) {
                        is Resource.Loading -> {
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

    /*inner class FirebaseOperationImpl : ProductListAdapter.FirestoreOperations {
        override fun addToWishlist(productModel: ProductHomeModel) {
            homeViewModel.addToWishlist(productModel)

        }

        override fun removeFromWishlist(productModel: ProductHomeModel) {
            homeViewModel.removeFromWishlist(productModel)
        }

        override  fun checkInWishlist(id: Int):Boolean {
             homeViewModel.checkInWishlist("wishlist",id)
            val status =homeViewModel.status
            Log.d("Statusinha",status.toString())
            return status
        }

    }*/
}
