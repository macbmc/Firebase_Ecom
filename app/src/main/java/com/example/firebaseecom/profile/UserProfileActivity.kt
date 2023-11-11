package com.example.firebaseecom.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.firebaseecom.CartOrder.ProductListActivity
import com.example.firebaseecom.R
import com.example.firebaseecom.auth.AuthViewModel
import com.example.firebaseecom.auth.SignUpActivity
import com.example.firebaseecom.databinding.ActivityUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint

class UserProfileActivity : AppCompatActivity() {
    private lateinit var activityUserProfileBinding: ActivityUserProfileBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        activityUserProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile)

        getUserdata()

        activityUserProfileBinding.apply {
            navPop.setOnClickListener{finish()}
            userLogout.setOnClickListener {
                userSignout()
            }
            orderLayout.setOnClickListener {
                navToCartOrders("orders")
            }
            cartLayout.setOnClickListener{
                navToCartOrders("cart")
            }
            editProfile.setOnClickListener{
                navToEditProfile()
            }

        }

    }

    private fun navToEditProfile() {
        val intent = Intent(this@UserProfileActivity, EditProfileActivity::class.java)
        intent.putExtra("user", activityUserProfileBinding.userDetails as Serializable)
        startActivity(intent)
    }

    private fun navToCartOrders(dest:String) {
        val intent = Intent(this@UserProfileActivity, ProductListActivity::class.java)
        intent.putExtra("dest", dest)
        startActivity(intent)
    }

    private fun userSignout() {
        val intent = Intent(this@UserProfileActivity, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        authViewModel.logout()
        finish()
    }

    private fun getUserdata() {
        lifecycleScope.launch {
            profileViewModel.getUserData()
            profileViewModel.userDetails.collect {
                activityUserProfileBinding.apply{
                    userDetails = it
                    if(userDetails?.userName?.isNotEmpty()!!)
                    {
                        greetingText.text=getString(R.string.hey)+userDetails?.userName
                    }
                    else
                    {
                        greetingText.text=getString(R.string.hey)+userDetails?.userEmail
                    }
                }

            }
        }

    }
}