package com.example.firebaseecom.offers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.FragmentOfferBinding
import com.example.firebaseecom.detailsPg.ProductDetailsActivity
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class PhoneFragment : Fragment() {

    private lateinit var fragmentPhoneBinding: FragmentOfferBinding
    private lateinit var offerZoneViewModel: OfferZoneViewModel
    private val adapter = OfferZoneAdapter(FragmentFunctionClass())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentPhoneBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_offer, container, false)
        offerZoneViewModel = ViewModelProvider(this)[OfferZoneViewModel::class.java]
        fragmentPhoneBinding.apply {
            val offerItemView = offerItemView
            offerItemView.adapter = adapter
            offerItemView.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        }

        observeOfferProducts()
        return fragmentPhoneBinding.root
    }

    private fun observeOfferProducts() {
        fragmentPhoneBinding.apply {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED)
                {
                    offerZoneViewModel.getOffersByCategory("Phone")
                    offerZoneViewModel.offerProductByCategory.collect {
                        when (it) {
                            is Resource.Success -> {
                                Log.d("offerZone", "success")
                                adapter.setProducts(it.data)
                                progressBar.visibility = View.GONE
                            }

                            is Resource.Loading -> {
                                Log.d("offerZone", "loading")
                                progressBar.isVisible = true
                            }

                            is Resource.Failed -> {
                                Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                                Log.d("offerZone", it.message)
                            }
                        }
                    }
                }
            }
        }
    }

    inner class FragmentFunctionClass : OfferZoneAdapter.FragmentFunctionInterface {
        override fun navToDetails(product: ProductHomeModel) {
            val intent = Intent(activity, ProductDetailsActivity::class.java)
            intent.putExtra("product", product as Serializable)
            startActivity(intent)
        }
    }


}