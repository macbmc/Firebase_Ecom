package com.example.firebaseecom.home

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.firebaseecom.CartOrder.ProductListActivity
import com.example.firebaseecom.R
import com.example.firebaseecom.category.ProductCategoryActivity
import com.example.firebaseecom.databinding.ActivityHomeBinding
import com.example.firebaseecom.detailsPg.ProductDetailsActivity
import com.example.firebaseecom.home.HomeViewModel.Companion.getChange
import com.example.firebaseecom.main.BaseActivity
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.offers.OfferZoneActivity
import com.example.firebaseecom.productSearch.ProductSearchActivity
import com.example.firebaseecom.profile.UserProfileActivity
import com.example.firebaseecom.utils.AlertDialogUtils
import com.example.firebaseecom.utils.NetworkState
import com.example.firebaseecom.utils.NetworkUtil
import com.example.firebaseecom.utils.Resource
import com.example.firebaseecom.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint


class HomeActivity : BaseActivity() {


    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private val carousalAdapter = CarousalAdapter(this@HomeActivity, ActivityFunctionClass())
    private val snapHelper = LinearSnapHelper()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("IN_APP_MESSAGING", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        checkForNewUser()
        observeNetwork()
        val adView = homeBinding.carousalView
        adView.adapter = carousalAdapter
        snapHelper.attachToRecyclerView(adView)
        adView.layoutManager = LinearLayoutManager(
            this@HomeActivity, LinearLayoutManager.HORIZONTAL,
            false
        )



        homeBinding.apply {
            cartNumber.text = "0"
            profButton.setOnClickListener {
                startActivity(Intent(this@HomeActivity, UserProfileActivity::class.java))
            }
            searchHomeButton.setOnClickListener {
                startActivity(Intent(this@HomeActivity, ProductSearchActivity::class.java))
            }
            cartHomeButton.setOnClickListener {
                val intent = Intent(this@HomeActivity, ProductListActivity::class.java)
                intent.putExtra("dest", getString(R.string.cart))
                startActivity(intent)
            }
            catLaptop.setOnClickListener {
                val intent = Intent(this@HomeActivity, ProductCategoryActivity::class.java)
                intent.putExtra("category", "Laptop")
                startActivity(intent)
            }
            catPhones.setOnClickListener {
                val intent = Intent(this@HomeActivity, ProductCategoryActivity::class.java)
                intent.putExtra("category", "Phone")
                startActivity(intent)
            }
            catTablet.setOnClickListener {
                val intent = Intent(this@HomeActivity, ProductCategoryActivity::class.java)
                intent.putExtra("category", "Tablet")
                startActivity(intent)
            }
        }


    }

    private fun loadOnRefresh() {
        observeNetwork()
    }

    private fun observeNetwork() {
        val network = NetworkUtil(this)
        homeBinding.networkProgress.visibility = View.VISIBLE
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                network.observeNetworkState().collect {
                    Log.d("networkState", it.toString())
                    when (it) {
                        NetworkState.AVAILABLE -> {
                            homeBinding.apply {

                                networkStatusLayout.visibility = View.GONE
                                homeLayout.isVisible = true
                                networkProgress.visibility = View.GONE

                            }
                            observeCarousal()
                            observeCartNumber()
                            observeProducts()
                        }

                        NetworkState.UNAVAILABLE -> {
                            homeBinding.apply {

                                networkText.text = getString(R.string.no_internet_connection)
                                networkStatusLayout.isVisible = true
                                homeLayout.isVisible = false
                                networkProgress.isVisible = true


                            }
                            ToastUtils().giveToast(
                                getString(R.string.connection_unavailable),
                                this@HomeActivity
                            )
                        }

                        NetworkState.LOSING -> {
                            homeBinding.apply {

                                networkText.text = getString(R.string.no_internet_connection)
                                networkStatusLayout.isVisible = true

                            }

                            ToastUtils().giveToast(
                                getString(R.string.connection_losing),
                                this@HomeActivity
                            )
                        }

                        NetworkState.LOST -> {
                            homeBinding.apply {

                                networkText.text = getString(R.string.no_internet_connection)
                                networkStatusLayout.isVisible = true

                            }
                            ToastUtils().giveToast(
                                getString(R.string.connection_lost),
                                this@HomeActivity
                            )
                        }
                    }
                }
            }
        }
    }

    private fun observeCarousal() {
        homeViewModel.adList.observe(this@HomeActivity) {
            carousalAdapter.setAd(it)
            homeBinding.carousalView.scrollToPosition(Integer.MAX_VALUE / 2)

        }
        homeViewModel.getAd()
    }


    private fun observeCartNumber() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val size = homeViewModel.checkNumbWishlist("cart")
                homeBinding.cartNumber.text = size.toString()
            }
        }
    }


    private fun observeProducts() {
        val homeItemView = homeBinding.homeItemView
        val adapter = ProductHomeAdapter(NavigateClass(), langId)

        homeItemView.layoutManager = GridLayoutManager(this@HomeActivity, 2)
        homeItemView.adapter = adapter
        homeBinding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
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
                                Log.d("itemViewLoader", "success")
                                adapter.setProduct(it.data)
                                observeNewProducts()

                            }

                            is Resource.Failed -> {
                                homeItemViewProgress.visibility = View.VISIBLE
                                Log.d("itemViewLoader", "Failed")
                                ToastUtils().giveToast(it.message, this@HomeActivity)
                            }


                        }

                    }
                }
            }
        }

    }

    inner class NavigateClass : ProductHomeAdapter.NavigationInterface {
        override fun navigateToDetails(productModel: ProductHomeModel) {
            val intent = Intent(this@HomeActivity, ProductDetailsActivity::class.java)
            intent.putExtra("product", productModel as Serializable)
            startActivity(intent)
        }

    }

    inner class ActivityFunctionClass : CarousalAdapter.ActivityFunctionInterface {
        override fun navToOfferZone() {
            startActivity(Intent(this@HomeActivity, OfferZoneActivity::class.java))
        }

    }

    override fun onRestart() {
        super.onRestart()
        observeNetwork()
    }

    private fun checkForNewUser() {
        if (sharedPreferences.getInt("newUserTrigger", 0) == 0) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    val isNewUser = homeViewModel.checkForNewUser()
                    Log.d("AlertDialogNewUser", isNewUser.toString())
                    if (isNewUser) {
                        editor.putInt("newUserTrigger", 1)
                        editor.apply()
                        Log.d("AlertDialogInsideNewUser", "called")
                        showNewUserDialog()
                    }
                }
            }
        }
    }


    private fun showNewUserDialog() {
        Log.d("ShowAlertDialog", "called")
        AlertDialogUtils().showAlertDialog(this, getString(R.string.enjoy_shopping))
    }

    private fun observeNewProducts() {
        Log.d("observeNewProducts", sharedPreferences.getInt("newProductTrigger", 0).toString())
        if (sharedPreferences.getInt("newProductTrigger", 0) == 0) {
            getChange.observe(this@HomeActivity) {
                if (it) {
                    editor.putInt("newProductTrigger", 1)
                    editor.apply()
                    AlertDialogUtils().showAlertDialog(
                        this@HomeActivity,
                        getString(R.string.new_products)
                    )
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        editor.putInt("newProductTrigger", 0)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        editor.putInt("newProductTrigger", 0)
        editor.apply()
    }
}


/*,
{
    "product_id": 110,
    "product_title": {
    "en": "Apple iPhone 15",
    "ml": "ആപ്പിൾ ഐഫോൺ 15 പ്രോ"
},
    "product_price": 135000,
    "product_category": "Phone",
    "product_image": "https://firebasestorage.googleapis.com/v0/b/imageapi-ecom.appspot.com/o/i-1-removebg-preview.png?alt=media&token=f0857e4c-d67b-4c74-a61a-d0670c627784"
}*/
