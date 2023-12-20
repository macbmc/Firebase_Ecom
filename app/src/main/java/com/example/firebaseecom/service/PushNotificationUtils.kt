package com.example.firebaseecom.service

import android.util.Log
import com.example.firebaseecom.utils.AlertDialogUtils
import com.example.firebaseecom.utils.NotificationUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationUtils : FirebaseMessagingService() {

    companion object{
        const val tag = "PUSH NOTIFICATION UTILS"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(tag, message.data.toString())
        AlertDialogUtils().showAlertDialog(this,message.notification.toString())

    }

}