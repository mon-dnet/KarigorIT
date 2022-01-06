package com.example.karigorit.http

import com.example.karigorit.GetUserResponse

interface AuthCallback {
    fun responseMsg(message: String)
    fun responseData(getUserResponse: GetUserResponse)
}
