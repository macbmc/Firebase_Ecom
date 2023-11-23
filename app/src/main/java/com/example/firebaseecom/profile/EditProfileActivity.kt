@file:Suppress("DEPRECATION")

package com.example.firebaseecom.profile

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.firebaseecom.R
import com.example.firebaseecom.auth.SignUpActivity
import com.example.firebaseecom.databinding.ActivityEditProfileBinding
import com.example.firebaseecom.model.UserModel
import com.example.firebaseecom.repositories.AuthRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    private lateinit var activityEditProfileBinding: ActivityEditProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var imgUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
            checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        ) {
            val permissionArray = arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            requestPermissions(permissionArray, 123)
        }
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->

                imgUri = result.data?.data!!
                Log.d("ImageCapture", imgUri.toString())
                Glide.with(this)
                    .load(imgUri.toString())
                    .error(R.drawable.placeholder_image)
                    .into(activityEditProfileBinding.userProfileImage)
                profileViewModel.storeImage(imgUri)

            }
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            Glide.with(this)
                .load(imgUri.toString())
                .error(R.drawable.placeholder_image)
                .into(activityEditProfileBinding.userProfileImage)
            Log.d("ImageCapture", imgUri.toString())
            profileViewModel.storeImage(imgUri)
        }
        activityEditProfileBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        profileViewModel = ViewModelProvider(this@EditProfileActivity)[ProfileViewModel::class.java]
        activityEditProfileBinding.apply {
            userDetails = intent.extras!!.get("user") as UserModel
            Glide.with(this@EditProfileActivity)
                .load(userDetails?.userImg)
                .error(R.drawable.ic_add)
                .into(userProfileImage)
            navPop.setOnClickListener {
                finish()
            }
            submitBtn.setOnClickListener {
                updateProfileData()
            }
            userProfileImage.setOnClickListener {
                saveUserProfileImage()

            }
        }
    }

    private fun saveUserProfileImage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.profile_image))
        builder.setPositiveButton(R.string.Gallery) { _, _ ->
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)

        }
        builder.setNeutralButton(R.string.Camera) { _, _ ->
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "ProfileImage")
            values.put(MediaStore.Images.Media.DESCRIPTION, "from camera")
            imgUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            cameraLauncher.launch(intent)

        }
        builder.setNegativeButton(R.string.cancel) { _, _ ->

        }
        builder.show()
    }

    private fun updateProfileData() {
        activityEditProfileBinding.apply {
            progressBar.isVisible = true

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
                        val user = UserModel(
                            editTextUsername.text.toString(), editTextEmail.text.toString(),
                            "", editTextPhone.text.toString(), editTextAddress.text.toString()
                        )
                        profileViewModel.updateUser(user)
                        Toast.makeText(this@EditProfileActivity, "Data Updated", Toast.LENGTH_SHORT)
                            .show()

                    }

                }
                builder.setNegativeButton(R.string.cancel) { _, _ ->

                }
                builder.show()
            } else {
                profileViewModel.getImageUrl()
                profileViewModel.userImageUrl.observe(this@EditProfileActivity) {
                    val user = UserModel(
                        editTextUsername.text.toString(), editTextEmail.text.toString(),
                        it, editTextPhone.text.toString(), editTextAddress.text.toString()
                    )
                    profileViewModel.updateUser(user)
                    progressBar.isVisible = false
                    Toast.makeText(this@EditProfileActivity, "Data Updated", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }


    }

    inner class AuthStateChangeImpl : AuthRepositoryImpl.AuthStateChange {
        override fun navToSignUp() {
            Log.d("After email verification", "navToSignUp called")
            val intent = Intent(this@EditProfileActivity, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

    }
}