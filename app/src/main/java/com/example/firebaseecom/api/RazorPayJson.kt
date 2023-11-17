package com.example.firebaseecom.api

import com.example.firebaseecom.R
import org.json.JSONObject

class RazorPayJson(private val totalAmount:Double) {

    var razorpayJSON = JSONObject()

    init {
        razorpayJSON.apply {
            put("name", "EKart")
            put("description", "Order Payment")
            put("theme.color", R.color.bgColor)
            put("currency", "INR")
            put("amount", totalAmount * 100)//paisaToRupee
        }
    }
}