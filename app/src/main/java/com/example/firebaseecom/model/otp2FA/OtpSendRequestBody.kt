package com.example.firebaseecom.model.otp2FA


import com.squareup.moshi.Json

data class OtpSendRequestBody(
    @Json(name = "content")
    val content: String?,
    @Json(name = "data_coding")
    val dataCoding: String?,
    @Json(name = "originator")
    val originator: String?,
    @Json(name = "recipient")
    val recipient: String?,
    @Json(name = "type")
    val type: String?
)