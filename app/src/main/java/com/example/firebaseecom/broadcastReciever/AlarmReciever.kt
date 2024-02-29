package com.example.firebaseecom.broadcastReciever

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.firebaseecom.R
import com.example.firebaseecom.api.EkartApiEndPoints
import com.example.firebaseecom.api.EkartApiService
import com.example.firebaseecom.auth.SignUpActivity
import com.example.firebaseecom.model.OfferModelClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@AndroidEntryPoint
class AlarmReciever : BroadcastReceiver() {

    private lateinit var nBuilder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private var activeOfferList = listOf<OfferModelClass>()
    private val ekartApiService =
        Retrofit.Builder().baseUrl(EkartApiEndPoints.END_POINT_BASE.url).addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        ).build().create(EkartApiService::class.java)
    private lateinit var apiCallActiveOffers: Response<List<OfferModelClass>?>


    override fun onReceive(context: Context, intent: Intent) {
        Log.d("IntentReceiver", intent.action.toString())
        val onClickIntent = Intent(context, SignUpActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, onClickIntent, PendingIntent.FLAG_IMMUTABLE)
        CoroutineScope(Dispatchers.IO).launch {
            activeOfferList = getOfferList()
            val activeOffer = activeOfferList.random()
            try {
                Glide.with(context)
                    .asBitmap()
                    .load(activeOffer.offerImage)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            val nManager =
                                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val notificationChannel =
                                    NotificationChannel(
                                        channelId,
                                        description,
                                        NotificationManager.IMPORTANCE_HIGH
                                    )
                                notificationChannel.enableLights(true)
                                notificationChannel.lightColor = context.getColor(R.color.bgColor)
                                notificationChannel.enableVibration(false)
                                nManager.createNotificationChannel(notificationChannel)

                                nBuilder = Notification.Builder(context, channelId)
                                nBuilder.setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(activeOffer.offerName)
                                    .setContentText(activeOffer.offerText)
                                    .setLargeIcon(resource)
                                    .setAutoCancel(true)
                                    .setContentIntent(pendingIntent)

                            } else {
                                nBuilder = Notification.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(activeOffer.offerName)
                                    .setContentText(activeOffer.offerText)
                                    .setLargeIcon(resource)
                                    .setAutoCancel(true)
                                    .setContentIntent(pendingIntent)

                            }
                            nManager.notify(99, nBuilder.build())
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                    })

            } catch (e: Exception) {
                Log.d("GlideNotification", e.toString())
            }
        }


    }


    private suspend fun getOfferList(): List<OfferModelClass> {

        val offerList = CoroutineScope(Dispatchers.IO).async {
            callApiAndShowNotification()
        }
        if (offerList.await() != null)
            return offerList.await()!!

        return listOf()


    }

    private suspend fun callApiAndShowNotification(): List<OfferModelClass>? {

        try {
            apiCallActiveOffers =
                ekartApiService.getSeasonalOffers(EkartApiEndPoints.END_POINT_OFFER_TYPES.url)
            Log.d("activeOffer", "success")
        } catch (e: Exception) {
            Log.d("activeOffer", e.toString())
        }
        if (::apiCallActiveOffers.isInitialized) {
            if (apiCallActiveOffers.code() != 200)
                return listOf()
            if (apiCallActiveOffers.body() != null)
                return apiCallActiveOffers.body()

        }
        return listOf()
    }
}