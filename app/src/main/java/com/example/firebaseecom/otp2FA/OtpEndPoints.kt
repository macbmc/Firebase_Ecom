package com.example.firebaseecom.otp2FA

enum class OtpEndPoints(val url:String) {
    OTP_BASE_URL("https://api.d7networks.com"),
    OTP_AUTH_TOKEN("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhdXRoLWJhY2tlbmQ6YXBwIiwic3ViIjoiMzQ4YzVmZjAtNjc3OS00YWJhLTg3N2QtNWM2Y2M1NjAxNmE2In0.A4icTPL3wHSWjVOXZUMngVQOlib_3gacfl2ov5rSLxU"),
    OTP_CONTENT_TYPE("application/json")
}