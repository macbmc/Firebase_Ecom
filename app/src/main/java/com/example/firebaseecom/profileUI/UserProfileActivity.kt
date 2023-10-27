package com.example.firebaseecom.profileUI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.firebaseecom.R
import com.example.firebaseecom.authUI.AuthViewModel
import com.example.firebaseecom.databinding.ActivityUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint

class UserProfileActivity : AppCompatActivity() {
    private lateinit var activityUserProfileBinding: ActivityUserProfileBinding
    private lateinit var authViewModel:AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel=ViewModelProvider(this).get(AuthViewModel::class.java)
        profileViewModel=ViewModelProvider(this).get(ProfileViewModel::class.java)
        activityUserProfileBinding=DataBindingUtil.setContentView(this,R.layout.activity_user_profile)
        getUserdata()
        activityUserProfileBinding.apply {
            userLogout.setOnClickListener{
                authViewModel.logout()
            }
        }
    }

    private fun getUserdata() {
        profileViewModel.getUserData()
        lifecycleScope.launch {
            profileViewModel.userDetails.collect{
                activityUserProfileBinding.userDetails = it
            }
        }

    }
}