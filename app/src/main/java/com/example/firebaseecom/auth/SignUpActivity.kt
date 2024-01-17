package com.example.firebaseecom.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivitySignUpBinding
import com.example.firebaseecom.home.HomeActivity
import com.example.firebaseecom.main.BaseActivity
import com.example.firebaseecom.utils.AlarmTriggerUtils
import com.example.firebaseecom.utils.Resource
import com.example.firebaseecom.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {
    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var authViewModel: AuthViewModel
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAlarmTrigger()
        authViewModel = ViewModelProvider(this@SignUpActivity)[AuthViewModel::class.java]
        signUpBinding =
            DataBindingUtil.setContentView(this@SignUpActivity, R.layout.activity_sign_up)
        Log.d("currentUserValue", authViewModel.currentUser.toString())
        if (authViewModel.currentUser != null) {
            Log.d("userId", authViewModel.currentUser!!.uid)
            val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        signUpBinding.apply {
            signUpButton.setOnClickListener {
                authSignUp()
            }
            toSignIn.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))

            }
        }
    }

    private fun setAlarmTrigger() {
        AlarmTriggerUtils().setAlarmTriggerForNotification(this)

    }

    private fun authSignUp() {
        signUpBinding.apply {
            job?.cancel()
            job = lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    authViewModel.apply {
                        signUp(
                            editSignupEmail.text.toString(),
                            editSignUpPassword.text.toString(),
                            editSignUpPhone.text.toString()
                        )
                        signUpAuth.collect {
                            when (it) {
                                is Resource.Loading -> {
                                    Log.d("Loading", "Loading")
                                    progressBar.visibility = View.VISIBLE
                                }

                                is Resource.Success -> {
                                    Log.d("success", "${it.data}")
                                    Log.d("userId", authViewModel.currentUser!!.uid)
                                    val intent =
                                        Intent(this@SignUpActivity, HomeActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()


                                }

                                is Resource.Failed -> {
                                    progressBar.isVisible = false
                                    Log.d("failed", it.message)
                                    ToastUtils().giveToast(it.message, this@SignUpActivity)
                                }

                                else -> {}

                            }
                        }
                    }


                }
            }
        }
    }

}
