package com.example.firebaseecom.utils

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import com.example.firebaseecom.R

class AlertDialogUtils {

     fun showAlertDialog(context: Context,dialogContent:String)
    {
        Log.d("AlertDialog","called")
        val alertDialog = AlertDialog.Builder(context)
        val optionDialog = alertDialog.create()
        val alertViewInflater =
            LayoutInflater.from(context).inflate(R.layout.newuser_alert_dialog, null)
        alertDialog.setView(alertViewInflater)
        alertViewInflater.findViewById<TextView>(R.id.dialogContent).text = dialogContent
        alertDialog.setPositiveButton(R.string.con) { _, _ ->
            optionDialog.dismiss()
        }
            .setCancelable(false)
        alertDialog.show()
    }
}