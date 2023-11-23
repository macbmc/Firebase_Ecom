package com.example.firebaseecom.utils

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Calendar

@HiltAndroidApp
@ExperimentalCoroutinesApi

class FirebaseEcomApp : Application() {

    private val localeHelper = LocaleHelperApplicationDelegate()
    override fun onCreate() {
        super.onCreate()
        val mode = if (isNight()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeHelper.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        /* newConfig.locale.apply {
             setLocale(this)
         }*/
        localeHelper.onConfigurationChanged(this)
    }

    /*private fun setLocale(newLocale: Locale) {
        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(newLocale)
    }*/

    private fun isNight(): Boolean {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return (currentHour <= 7 || currentHour >= 18)


    }
    override fun getApplicationContext(): Context =
        LocaleHelper.onAttach(super.getApplicationContext())
}
