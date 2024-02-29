package com.example.firebaseecom.model.otp2FA

import com.squareup.moshi.Json

data class OtpSendResponseBody(

    @Json(name = "otp_id")
    val otpId : String,
    @Json(name = "status")
    val status : String
)
