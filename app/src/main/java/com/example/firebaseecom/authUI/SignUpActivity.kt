package com.example.firebaseecom.authUI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivitySignUpBinding
import com.example.firebaseecom.homeUI.HomeActivity
import com.example.firebaseecom.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProvider(this@SignUpActivity).get(AuthViewModel::class.java)
        signUpBinding =
            DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)
        if (authViewModel.currentUser != null) {
            Log.d("userId", "${authViewModel.currentUser!!.uid}")
            startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
        }

        signUpBinding.apply {
            signUpButton.setOnClickListener {
                lifecycleScope.launch {
                    authViewModel.apply {
                        signUp(editSignupEmail.text.toString(), editSignUpPassword.text.toString())
                        signUpAuth.collect {
                            when (it) {
                                is Resource.Loading -> {
                                    Log.d("Loading", "Loading")
                                    progressBar.visibility= View.VISIBLE
                                }

                                is Resource.Success -> {
                                    Log.d("success", "${it.data}")
                                    Log.d("userId", "${authViewModel.currentUser!!.uid}")
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            HomeActivity::class.java
                                        )
                                    )

                                }

                                is Resource.Failed -> {
                                    Log.d("failed", "${it.message}")
                                    Toast.makeText(this@SignUpActivity,"${it.message}",
                                        Toast.LENGTH_SHORT).show()
                                }

                                else -> {}

                            }
                        }
                    }


                }
            }
            toSignIn.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))

            }
        }
    }
}