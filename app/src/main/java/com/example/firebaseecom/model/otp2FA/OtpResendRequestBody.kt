package com.example.firebaseecom.model.otp2FA

import com.squareup.moshi.Json

data class OtpResendRequestBody(
    @Json(name = "otp_id")
    val otpId : String
)
