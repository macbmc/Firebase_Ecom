package com.example.firebaseecom.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Calendar

@HiltAndroidApp
@ExperimentalCoroutinesApi

class FirebaseEcomApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val mode = if (!isNight()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun isNight(): Boolean {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return (currentHour <= 7 || currentHour >= 18)


    }
}
