package com.example.firebaseecom.model.otp2FA

import com.squareup.moshi.Json

data class OtpVerifyRequestBody(

    @Json(name = "otp_id")
    val otpId : String,
    @Json(name = "otp_code")
    val otpCode:String
)
