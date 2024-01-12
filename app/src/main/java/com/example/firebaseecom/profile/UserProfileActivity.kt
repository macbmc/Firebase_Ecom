package com.example.firebaseecom.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.firebaseecom.CartOrder.ProductListActivity
import com.example.firebaseecom.R
import com.example.firebaseecom.auth.AuthViewModel
import com.example.firebaseecom.auth.SignUpActivity
import com.example.firebaseecom.customerChat.CustomerChatActivity
import com.example.firebaseecom.databinding.ActivityUserProfileBinding
import com.example.firebaseecom.main.BaseActivity
import com.example.firebaseecom.utils.AlertDialogUtils
import com.example.firebaseecom.utils.FirebaseEcomApp
import com.example.firebaseecom.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.Serializable
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint

class UserProfileActivity : BaseActivity() {
    private lateinit var activityUserProfileBinding: ActivityUserProfileBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("IN_APP_MESSAGING", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        activityUserProfileBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_user_profile)
        getUserdata()

        activityUserProfileBinding.apply {

            darkModeBtn.setOnClickListener { changeMode() }

            navPop.setOnClickListener { finish() }
            userLogout.setOnClickListener {
                userSignout()
            }
            orderLayout.setOnClickListener {
                navToCartOrders(getString(R.string.order))
            }
            cartLayout.setOnClickListener {
                navToCartOrders(getString(R.string.cart))
            }
            editProfile.setOnClickListener {
                navToEditProfile()
            }
            chatQuery.setOnClickListener{
                navToChat()
            }
            malayalamLanguageLayout.setOnClickListener {

                ToastUtils().giveToast(getString(R.string.malayalam), this@UserProfileActivity)

                changeLocale("ml")

            }
            englishLanguageLayout.setOnClickListener {

                ToastUtils().giveToast(getString(R.string.english), this@UserProfileActivity)

                changeLocale("en")
            }

            appPoints.setOnClickListener {
                startActivity(Intent(this@UserProfileActivity,UserReviewActivity::class.java))
            }

        }

    }

    private fun navToChat() {
        startActivity(Intent(this,CustomerChatActivity::class.java))
    }

    private fun changeMode() {
        FirebaseEcomApp().changeMode()
    }


    override fun onRestart() {
        super.onRestart()
        getUserdata()
    }

    private fun navToEditProfile() {
        if (activityUserProfileBinding.userDetails != null) {
            val intent = Intent(this@UserProfileActivity, EditProfileActivity::class.java)
            intent.putExtra("user", activityUserProfileBinding.userDetails as Serializable)
            startActivity(intent)
        }
    }

    private fun navToCartOrders(dest: String) {
        val intent = Intent(this@UserProfileActivity, ProductListActivity::class.java)
        intent.putExtra("dest", dest)
        startActivity(intent)
    }

    private fun userSignout() {
        editor.putInt("newUserTrigger", 0)
        editor.apply()
        val intent = Intent(this@UserProfileActivity, SignUpActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        authViewModel.logout()
        finish()
    }

    private fun getUserdata() {

        profileViewModel.userData()

        profileViewModel.userDetails.observe(this) {
            Log.d("userDialog", "called")
            activityUserProfileBinding.apply {
                userDetails = it
                Log.d("userDialog", sharedPreferences.getInt("userProfileDialog", 0).toString())
                if (userDetails?.userName!!.isEmpty() || userDetails?.userImg!!.isEmpty() || userDetails?.address!!.isEmpty()) {
                    if (sharedPreferences.getInt("userProfileDialog", 0) == 0) {
                        showProfileCompleteDialog()
                        editor.putInt("userProfileDialog", 1)
                        editor.apply()
                    }

                }

                if (userDetails?.userName!!.isNotEmpty()) {
                    greetingText.text = getString(R.string.hey, userDetails?.userName)
                }
                Glide.with(this@UserProfileActivity)
                    .load(userDetails?.userImg)
                    .error(R.drawable.placeholder_image)
                    .into(userImage)
            }
        }


    }

    private fun showProfileCompleteDialog() {
        AlertDialogUtils().showAlertDialogNotification(
            this@UserProfileActivity,
            getString(R.string.completeProfileMsg)
        )
    }


    private fun changeLocale(langId: String) {
        val newLocale = Locale(langId)
        localeDelegate.setLocale(this, newLocale)


    }

    override fun onResume() {
        super.onResume()
        getUserdata()
    }


}
