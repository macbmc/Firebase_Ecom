package com.example.firebaseecom.repositories

import android.util.Log
import com.example.firebaseecom.model.otp2FA.OtpResendRequestBody
import com.example.firebaseecom.model.otp2FA.OtpResendResponseBody
import com.example.firebaseecom.model.otp2FA.OtpSendRequestBody
import com.example.firebaseecom.model.otp2FA.OtpSendResponseBody
import com.example.firebaseecom.model.otp2FA.OtpVerifyRequestBody
import com.example.firebaseecom.otp2FA.OtpEndPoints
import com.example.firebaseecom.otp2FA.OtpService
import com.example.firebaseecom.utils.Resource
import retrofit2.Response
import javax.inject.Inject

interface OtpRepository {

    suspend fun sendOtp(requestBody: OtpSendRequestBody): Resource<OtpSendResponseBody>
    suspend fun resendOtp(resendRequestBody: OtpResendRequestBody): Resource<OtpResendResponseBody>
    suspend fun verifyOtp(otpCode: String): Resource<Boolean>
}


class OtpRepositoryImpl @Inject constructor(
    private val otpRetrofit: OtpService
) : OtpRepository {
    private lateinit var otpSendResponseBody: Response<OtpSendResponseBody>
    private lateinit var otpVerifyResponseBody: Response<Any>

    companion object {
        var otpResponseCode = ""
    }


    override suspend fun sendOtp(requestBody: OtpSendRequestBody): Resource<OtpSendResponseBody> {

        try {
            otpSendResponseBody = otpRetrofit.sendOtp(
                OtpEndPoints.OTP_AUTH_TOKEN.url, OtpEndPoints.OTP_CONTENT_TYPE.url,
                requestBody
            )
            Log.d("phoneAuth", otpSendResponseBody.raw().toString())

        } catch (e: Exception) {
            Log.d("phoneAuth", e.toString())
        }

        if (::otpSendResponseBody.isInitialized) {
            if (otpSendResponseBody.code() != 200)
                return Resource.Failed("Credentials not valid")
            otpResponseCode = otpSendResponseBody.body()!!.otpId
            return Resource.Success(otpSendResponseBody.body()!!)
        } else
            return Resource.Failed("Credentials not valid")
    }

    override suspend fun resendOtp(resendRequestBody: OtpResendRequestBody): Resource<OtpResendResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyOtp(otpCode: String): Resource<Boolean> {
        val verifyRequestBody = OtpVerifyRequestBody(otpResponseCode, otpCode)
        try {
            otpVerifyResponseBody = otpRetrofit.verifyOtp(
                OtpEndPoints.OTP_AUTH_TOKEN.url, OtpEndPoints.OTP_CONTENT_TYPE.url,
                verifyRequestBody
            )
            Log.d("phoneAuthV", otpVerifyResponseBody.raw().toString())

        } catch (e: Exception) {
            Log.d("phoneAuthV", e.toString())
        }

        if (::otpVerifyResponseBody.isInitialized) {
            if (otpVerifyResponseBody.code() != 200)
                return Resource.Failed("Otp not valid")
            return Resource.Success(true)
        } else
            return Resource.Failed("Otp not valid")
    }
}
