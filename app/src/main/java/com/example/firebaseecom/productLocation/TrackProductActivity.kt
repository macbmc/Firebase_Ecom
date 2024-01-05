package com.example.firebaseecom.productLocation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityTrachProductBinding
import com.example.firebaseecom.model.ProductOrderModel
import com.example.firebaseecom.utils.OsmdroidUtils
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView

@AndroidEntryPoint

@Suppress("IMPLICIT_CAST_TO_ANY")
class TrackProductActivity : AppCompatActivity() {
    private lateinit var activityTracKProductBinding: ActivityTrachProductBinding
    private lateinit var recreateIntent: Intent
    private val osmUtil = OsmdroidUtils(this@TrackProductActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTracKProductBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_trach_product)
        val productOrder = intent.extras!!.get("product") as ProductOrderModel
        recreateIntent = intent
        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )
        activityTracKProductBinding.apply {
            navPop.setOnClickListener { finish() }
            locationRefresh.setOnClickListener {
                finish()
                startActivity(recreateIntent)
            }
            getCurrentLocation(productOrder)
            getLocationMap(osmView, productOrder)

        }
    }

    private fun getCurrentLocation(productOrder: ProductOrderModel) {
        osmUtil.getLocationFromGeoPoint(productOrder).observe(this) { locationInfo ->
            when (locationInfo) {
                "" -> {
                    finish()
                    startActivity(recreateIntent)
                }

                else -> {

                    this@TrackProductActivity.runOnUiThread {
                        activityTracKProductBinding.productLocation.text =
                            getString(R.string.location_info, locationInfo)
                    }


                }
            }

        }

    }

    private fun getLocationMap(osmView: MapView, productOrder: ProductOrderModel) {
        osmUtil.showTrackProduct(osmView, productOrder).observe(this) { mapStatus ->
            Log.d("mapLoaded", mapStatus)
            when (mapStatus) {
                "success" -> {
                    activityTracKProductBinding.mapProgress.visibility = View.GONE

                }

                else -> {
                    finish()
                    startActivity(recreateIntent)
                }
            }

        }
    }


}