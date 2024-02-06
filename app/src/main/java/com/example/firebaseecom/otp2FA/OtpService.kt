package com.example.firebaseecom.otp2FA

import com.example.firebaseecom.model.otp2FA.OtpResendRequestBody
import com.example.firebaseecom.model.otp2FA.OtpResendResponseBody
import com.example.firebaseecom.model.otp2FA.OtpSendRequestBody
import com.example.firebaseecom.model.otp2FA.OtpSendResponseBody
import com.example.firebaseecom.model.otp2FA.OtpVerifyRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OtpService {

    @POST("/verify/v1/otp/send-otp")
    suspend fun sendOtp(
        @Header("Authorization") authorizationKey: String,
        @Header("Content-Type") contentType: String,
        @Body() requestBody: OtpSendRequestBody
    ): Response<OtpSendResponseBody>

    @POST("/verify/v1/otp/resend-otp")
    suspend fun resendOtp(
        @Header("Authorization") authorizationKey: String,
        @Header("Content-Type") contentType: String,
        @Body() requestBody: OtpResendRequestBody
    )

    @POST("/verify/v1/otp/verify-otp")
    suspend fun verifyOtp(
        @Header("Authorization") authorizationKey: String,
        @Header("Content-Type") contentType: String,
        @Body() requestBody: OtpVerifyRequestBody
    ):Response<Any>

}