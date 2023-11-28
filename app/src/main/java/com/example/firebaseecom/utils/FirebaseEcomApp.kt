package com.example.firebaseecom.utils

import android.app.Application
import android.content.Context

import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log

import androidx.appcompat.app.AppCompatDelegate
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Calendar

@HiltAndroidApp
@ExperimentalCoroutinesApi


open class FirebaseEcomApp : Application() {

    private val localeHelper = LocaleHelperApplicationDelegate()
    private var mode = 0

    companion object{
        lateinit var sharedPreferences: SharedPreferences
        lateinit var editor : SharedPreferences.Editor
    }
    override fun onCreate() {
        super.onCreate()

        sharedPreferences=getSharedPreferences("Theme_Shared_Preferences", MODE_PRIVATE)
        editor = sharedPreferences.edit()!!


        mode = sharedPreferences.getInt("theme",AppCompatDelegate.MODE_NIGHT_NO)
        Log.d("theme",mode.toString())
        darkMode()
    }

    private fun darkMode() {
        when(mode)
        {
           1 ->
           {
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
           }
           2 ->
           {
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
           }

        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeHelper.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeHelper.onConfigurationChanged(this)
    }



    private fun isNight(): Boolean {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return (currentHour <= 7 || currentHour >= 18)


    }
    override fun getApplicationContext(): Context =
        LocaleHelper.onAttach(super.getApplicationContext())


     fun changeMode(){
         when(sharedPreferences.getInt("theme",AppCompatDelegate.MODE_NIGHT_NO))
         {
             1 ->
             {
                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                 editor.putInt("theme",AppCompatDelegate.MODE_NIGHT_YES)
                 editor.commit()
                 editor.apply()
             }
             2->
             {
                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                 editor.putInt("theme",AppCompatDelegate.MODE_NIGHT_NO)
                 editor.commit()
                 editor.apply()
             }
             else->
             {
                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
             }
         }



    }

}
