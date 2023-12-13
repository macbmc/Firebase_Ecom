package com.example.firebaseecom.offers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityOfferZoneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferZoneActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private lateinit var activityOfferZoneBinding: ActivityOfferZoneBinding
    private val fragmentTransaction = fragmentManager.beginTransaction()
    private val phoneFragment = PhoneFragment()
    private val laptopFragment = LaptopFragment()
    private val tabletFragment = TabletFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOfferZoneBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_offer_zone)
        activityOfferZoneBinding.apply {
            appbarTitle.text=getString(R.string.offerZone)
            fragmentTransaction.add(offerLayout.id, phoneFragment)
                .setTransition(TRANSIT_FRAGMENT_OPEN).commit()
            navPop.setOnClickListener {
                finish()
            }
            laptopFragmentBtn.setOnClickListener {
                changeFragment(laptopFragment)
            }
            phoneFragmentBtn.setOnClickListener {
                changeFragment(phoneFragment)
            }
            tabletFragmentBtn.setOnClickListener {
                changeFragment(tabletFragment)
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(activityOfferZoneBinding.offerLayout.id, fragment)
            .addToBackStack(null)
            .setTransition(TRANSIT_FRAGMENT_FADE).commit()
    }
}


