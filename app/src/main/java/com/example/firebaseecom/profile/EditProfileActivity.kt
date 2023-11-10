@file:Suppress("DEPRECATION")

package com.example.firebaseecom.profile

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseecom.R
import com.example.firebaseecom.auth.LoginActivity
import com.example.firebaseecom.databinding.ActivityEditProfileBinding
import com.example.firebaseecom.model.UserModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    private lateinit var activityEditProfileBinding: ActivityEditProfileBinding
    lateinit var profileViewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEditProfileBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        profileViewModel = ViewModelProvider(this@EditProfileActivity)[ProfileViewModel::class.java]
        activityEditProfileBinding.apply {
            userDetails = intent.extras!!.get("user") as UserModel
            navPop.setOnClickListener {
                finish()
            }
            submitBtn.setOnClickListener {
                updateProfileData()
            }
        }
    }

    private fun updateProfileData() {
        activityEditProfileBinding.apply {
            if (userDetails?.userEmail != editTextEmail.text.toString()) {
                val builder = AlertDialog.Builder(this@EditProfileActivity)
                val editText = EditText(this@EditProfileActivity)
                builder.setTitle(getString(R.string.profile_update))
                builder.setMessage(getString(R.string.to_continue_please_enter_password))
                builder.setView(editText)
                builder.setPositiveButton(R.string.submit) { _, _ ->
                    val password = editText.text.toString()
                    activityEditProfileBinding.apply {
                        profileViewModel.updateUserEmail(editTextEmail.text.toString(), password)
                        val user = UserModel(editTextUsername.text.toString(),editTextEmail.text.toString(),
                            "",editTextPhone.text.toString(),editTextAddress.text.toString())
                        profileViewModel.updateUser(user)
                        Toast.makeText(this@EditProfileActivity,"Data Updated",Toast.LENGTH_SHORT)
                            .show()
                       /* startActivity(Intent(this@EditProfileActivity,LoginActivity::class.java))*/

                    }

                }
                builder.setNegativeButton(R.string.cancel) { _, _ ->

                }
                builder.show()
            }
            else
            {
                val user = UserModel(editTextUsername.text.toString(),editTextEmail.text.toString(),
                    "",editTextPhone.text.toString(),editTextAddress.text.toString())
                profileViewModel.updateUser(user)
                Toast.makeText(this@EditProfileActivity,"Data Updated",Toast.LENGTH_SHORT)
                    .show()
                startActivity(Intent(this@EditProfileActivity,UserProfileActivity::class.java))
            }
        }


    }
}