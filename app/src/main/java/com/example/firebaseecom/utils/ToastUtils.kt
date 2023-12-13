package com.example.firebaseecom.utils

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class ToastUtils {

    fun giveToast(msg: String,context:Context) {

        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()

    }
}