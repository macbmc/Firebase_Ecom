package com.example.firebaseecom.model.otp2FA

import com.squareup.moshi.Json

data class OtpResendResponseBody(

    @Json(name = "otp_id")
    val otpId : String,
    @Json(name = "status")
    val status:String,
    @Json(name = "expiry")
    val expiry : String,
    @Json(name = "resend_count")
    val resendCount: String
)
