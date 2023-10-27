package com.example.firebaseecom.profileUI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseecom.R
import com.example.firebaseecom.authUI.AuthViewModel
import com.example.firebaseecom.databinding.ActivityUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class UserProfileActivity : AppCompatActivity() {
    private lateinit var activityUserProfileBinding: ActivityUserProfileBinding
    private lateinit var authViewModel:AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel=ViewModelProvider(this).get(AuthViewModel::class.java)
        activityUserProfileBinding=DataBindingUtil.setContentView(this,R.layout.activity_user_profile)
        activityUserProfileBinding.apply {
            userLogout.setOnClickListener{
                authViewModel.logout()

            }
        }
    }
}