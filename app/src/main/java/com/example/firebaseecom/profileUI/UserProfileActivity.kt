package com.example.firebaseecom.profileUI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.firebaseecom.R
import com.example.firebaseecom.authUI.AuthViewModel
import com.example.firebaseecom.authUI.SignUpActivity
import com.example.firebaseecom.databinding.ActivityUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class UserProfileActivity : AppCompatActivity() {
    private lateinit var activityUserProfileBinding: ActivityUserProfileBinding
    private lateinit var authViewModel:AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel= ViewModelProvider(this)[AuthViewModel::class.java]
        profileViewModel= ViewModelProvider(this)[ProfileViewModel::class.java]
        activityUserProfileBinding=DataBindingUtil.setContentView(this,R.layout.activity_user_profile)
        getUserdata()
        activityUserProfileBinding.apply {
            userLogout.setOnClickListener{

               val intent= Intent(this@UserProfileActivity,SignUpActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                authViewModel.logout()
                finish()
            }
            navPop.setOnClickListener{
                finish()
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