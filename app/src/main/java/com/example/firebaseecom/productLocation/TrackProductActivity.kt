package com.example.firebaseecom.productLocation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseecom.R
import com.example.firebaseecom.model.ProductHomeModel
import com.example.firebaseecom.model.ProductOrderModel
import com.example.firebaseecom.utils.OsmdroidUtils
import org.osmdroid.config.Configuration

class TrackProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trach_product)
        val productOrder = intent.extras!!.get("product") as ProductOrderModel
        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )
        OsmdroidUtils(this).showTrackProduct(findViewById(R.id.osmView),productOrder)
    }
}