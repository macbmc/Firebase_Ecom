package com.example.firebaseecom.offers

import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityOfferZoneBinding
import com.example.firebaseecom.main.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferZoneActivity : BaseActivity() {

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
            phoneFragmentBtn.background =
                AppCompatResources.getDrawable(this@OfferZoneActivity, R.drawable.border_onclicked)
            appbarTitle.text = getString(R.string.offerZone)
            fragmentTransaction.add(offerLayout.id, phoneFragment)
                .setTransition(TRANSIT_FRAGMENT_OPEN).commit()
            navPop.setOnClickListener {
                finish()
            }
            laptopFragmentBtn.setOnClickListener {
                laptopFragmentBtn.background = AppCompatResources.getDrawable(
                    this@OfferZoneActivity,
                    R.drawable.border_onclicked
                )
                phoneFragmentBtn.background = AppCompatResources.getDrawable(
                    this@OfferZoneActivity,
                    R.drawable.border_edit_profile
                )
                tabletFragmentBtn.background = AppCompatResources.getDrawable(
                    this@OfferZoneActivity,
                    R.drawable.border_edit_profile
                )
                changeFragment(laptopFragment)
            }
            phoneFragmentBtn.setOnClickListener {
                laptopFragmentBtn.background = AppCompatResources.getDrawable(
                    this@OfferZoneActivity,
                    R.drawable.border_edit_profile
                )
                phoneFragmentBtn.background = AppCompatResources.getDrawable(
                    this@OfferZoneActivity,
                    R.drawable.border_onclicked
                )
                tabletFragmentBtn.background = AppCompatResources.getDrawable(
                    this@OfferZoneActivity,
                    R.drawable.border_edit_profile
                )
                changeFragment(phoneFragment)
            }
            tabletFragmentBtn.setOnClickListener {
                laptopFragmentBtn.background = AppCompatResources.getDrawable(
                    this@OfferZoneActivity,
                    R.drawable.border_edit_profile
                )
                phoneFragmentBtn.background = AppCompatResources.getDrawable(
                    this@OfferZoneActivity,
                    R.drawable.border_edit_profile
                )
                tabletFragmentBtn.background = AppCompatResources.getDrawable(
                    this@OfferZoneActivity,
                    R.drawable.border_onclicked
                )
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


