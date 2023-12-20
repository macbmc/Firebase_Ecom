package com.example.firebaseecom.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.firebaseecom.R
import com.example.firebaseecom.model.OfferModelClass

class NotificationUtils(
    private val context: Context
) {
    private lateinit var nBuilder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    fun showNotification(pendingIntent: PendingIntent, msg: String) {

        try {
            Log.d("Status Notification", "try")
            val nManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel =
                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = context.getColor(R.color.bgColor)
                notificationChannel.enableVibration(false)
                nManager.createNotificationChannel(notificationChannel)

                nBuilder = Notification.Builder(context, channelId)
                nBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(msg)

            } else {
                nBuilder = Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(msg)

            }
            nManager.notify(101, nBuilder.build())
        } catch (e: Exception) {
            Log.d("Status Notification", e.toString())
        }


    }

    fun showOfferNotificationWithImage(
        activeOfferList: List<OfferModelClass>?,
        pendingIntent: PendingIntent
    ) {
        val n = (0..activeOfferList?.size!!.minus(1)).random()
        val activeOffer = activeOfferList[n]

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
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)

                        } else {
                            nBuilder = Notification.Builder(context)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(activeOffer.offerName)
                                .setContentText(activeOffer.offerText)
                                .setLargeIcon(resource)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)

                        }
                        nManager.notify(102, nBuilder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }

                })

        } catch (e: Exception) {
            Log.d("GlideNotification", e.toString())
        }

    }
}

