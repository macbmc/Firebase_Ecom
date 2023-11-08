package com.example.firebaseecom.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

@AndroidEntryPoint

class UserProfileActivity : AppCompatActivity() {
    private lateinit var activityUserProfileBinding: ActivityUserProfileBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        activityUserProfileBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_user_profile)
        getUserdata()
        activityUserProfileBinding.apply {
            viewOrders.setOnClickListener{
                val intent = Intent(this@UserProfileActivity,ProductListActivity::class.java)
                intent.putExtra("dest","orders")
                startActivity(intent)
            }
            userLogout.setOnClickListener {
                val intent = Intent(this@UserProfileActivity, SignUpActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                authViewModel.logout()
                finish()
            }
            navPop.setOnClickListener {
                finish()
            }
            editProfile.setOnClickListener{
                profileView.isVisible=false
                profileEditView.isVisible=true
            }
        }
    }

    private fun getUserdata() {
        lifecycleScope.launch {
            profileViewModel.getUserData()
            profileViewModel.userDetails.collect {
                Log.d("userData",it.toString())
                activityUserProfileBinding.userDetails = it
            }
        }

    }
}