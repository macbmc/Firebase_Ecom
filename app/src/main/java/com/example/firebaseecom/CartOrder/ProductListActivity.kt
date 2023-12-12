package com.example.firebaseecom.CartOrder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.firebaseecom.model.ProductOrderModel
import com.example.firebaseecom.payments.ProductCheckoutActivity
import com.example.firebaseecom.utils.Resource
import com.example.firebaseecom.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class ProductListActivity : BaseActivity() {
    private lateinit var activityProductListBinding: ActivityProductListBinding
    private lateinit var productListViewModel: ProductListViewModel
    private lateinit var cartAdapter: ProductCartAdapter
    private lateinit var orderAdapter: ProductOrderAdapter
    var productList = arrayListOf<ProductHomeModel>()
    private var dest = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_list)
        productListViewModel = ViewModelProvider(this)[ProductListViewModel::class.java]
        dest = intent.getStringExtra("dest")!!
        cartAdapter = ProductCartAdapter(ActivityFunctionClass(), langId)
        orderAdapter = ProductOrderAdapter(NavToClass(), langId)
        observeProducts(dest)
        activityProductListBinding.apply {
            if (dest == getString(R.string.order)) {
                ButtonHolder.visibility = View.GONE
                recyclerView.adapter = orderAdapter
            } else {
                recyclerView.adapter = cartAdapter
            }
            backButton.setOnClickListener {
                finish()
            }
            destText.text = dest
            changeHeading()
            recyclerView.layoutManager = LinearLayoutManager(
                this@ProductListActivity, LinearLayoutManager.VERTICAL,
                false
            )
            buttonBuyNow.setOnClickListener {
                if (productList.isNotEmpty()) {
                    val intent =
                        Intent(this@ProductListActivity, ProductCheckoutActivity::class.java)
                    intent.putExtra("productList", productList)
                    Log.d("productList", productList.toString())
                    startActivity(intent)
                } else {
                    ToastUtils().giveToast(
                        getString(R.string.add_items_to_cart_first),
                        this@ProductListActivity,
                    )
                }

            }

        }


    }

    private fun changeHeading() {
        when (dest) {
            "cart" -> {
                activityProductListBinding.destText.text = getString(R.string.cart)
            }

            "orders" -> {
                activityProductListBinding.destText.text = getString(R.string.order)
            }
        }

    }

    private fun observeProducts(dest: String?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (dest == getString(R.string.order)) {
                    productListViewModel.getProductFromOrder()
                    productListViewModel.productOrderList.collect {
                        when (it) {
                            is Resource.Loading -> {
                                activityProductListBinding.progressBar.isVisible = true
                            }

                            is Resource.Success -> {
                                activityProductListBinding.progressBar.isVisible = false
                                Log.d("cartData", it.data.toString())
                                orderAdapter.setProduct(it.data)

                            }

                            is Resource.Failed -> {
                                activityProductListBinding.progressBar.isVisible = false
                                ToastUtils().giveToast(it.message, this@ProductListActivity)
                            }
                        }
                    }
                } else {
                    if (dest == getString(R.string.cart)) {
                        productListViewModel.getProductFromDest("cart")
                        productListViewModel.productCartList.collect {
                            when (it) {
                                is Resource.Loading -> {
                                    activityProductListBinding.progressBar.isVisible = true
                                }

                                is Resource.Success -> {
                                    activityProductListBinding.progressBar.isVisible = false
                                    Log.d("cartData", it.data.toString())
                                    cartAdapter.setProduct(it.data)
                                    productList = ArrayList(it.data)

                                }

                                is Resource.Failed -> {
                                    activityProductListBinding.progressBar.isVisible = false
                                    ToastUtils().giveToast(it.message, this@ProductListActivity)
                                }
                            }
                        }
                    }

                }
            }

        }


    }

    inner class ActivityFunctionClass : ProductCartAdapter.ActivityFunctionInterface {
        override fun navigateToDetails(productHomeModel: ProductHomeModel) {
            val intent = Intent(this@ProductListActivity, ProductDetailsActivity::class.java)
            intent.putExtra("product", productHomeModel as Serializable)
            startActivity(intent)
        }

        override fun deleteFromCart(productHomeModel: ProductHomeModel, position: Int) {


            productListViewModel.removeFromCart(productHomeModel)
            productList.remove(productHomeModel)


        }


        override fun addTotalPrice(productList: List<ProductHomeModel>) {
            var totalPrice = 0
            for (product in productList) {
                totalPrice += product.productPrice!!
            }
            activityProductListBinding.cartPrice.text =
                getString(R.string.price, totalPrice.toString())
        }
    }


    inner class NavToClass : ProductOrderAdapter.navInterface {
        override fun navToOrderView(productHomeModel: ProductOrderModel) {
            val intent = Intent(this@ProductListActivity, ProductOrderViewActivity::class.java)
            intent.putExtra("productDetails", productHomeModel)
            startActivity(intent)
        }

    }
}