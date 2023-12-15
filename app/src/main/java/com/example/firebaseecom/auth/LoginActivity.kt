package com.example.firebaseecom.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.firebaseecom.R
import com.example.firebaseecom.databinding.ActivityLoginBinding
import com.example.firebaseecom.home.HomeActivity
import com.example.firebaseecom.main.BaseActivity
import com.example.firebaseecom.utils.Resource
import com.example.firebaseecom.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(
            this@LoginActivity, R.layout.activity_login
        )
        authViewModel = ViewModelProvider(this@LoginActivity)[AuthViewModel::class.java]

        activityLoginBinding.apply {
            logInButton.setOnClickListener {
                authLogin()
            }
            toSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }
            forgotPassword.setOnClickListener {
                authForgotPassword()
            }
        }
    }

    private fun authForgotPassword() {
        val builder = AlertDialog.Builder(this)
        val editText = EditText(this)
        builder.setTitle(getString(R.string.forgot_password))
        builder.setMessage(getString(R.string.enter_your_email_linked_to_ekart))
        builder.setView(editText)
        builder.setPositiveButton(R.string.submit) { _, _ ->
            authViewModel.forgotPassword(editText.text.toString())
        }
        builder.setNegativeButton(R.string.cancel) { _, _ -> }
        builder.show()
    }

    private fun authLogin() {
        job?.cancel()
        activityLoginBinding.apply {
            job = lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    authViewModel.apply {
                        logIn(editTextUsername.text.toString(), editTextPassword.text.toString())
                        loginAuth.collect {
                            when (it) {
                                is Resource.Loading -> {
                                    Log.d("Loading", "Loading")
                                    progressBar.visibility = View.VISIBLE
                                }

                                is Resource.Success -> {
                                    Log.d("success", "${it.data}")
                                    val intent =
                                        Intent(this@LoginActivity, HomeActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()

                                }

                                is Resource.Failed -> {
                                    Log.d("failed", it.message)
                                    progressBar.visibility = View.INVISIBLE
                                    ToastUtils().giveToast(it.message, this@LoginActivity)
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