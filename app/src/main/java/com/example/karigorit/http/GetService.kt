package com.example.karigorit.http

import com.example.karigorit.GetUserResponse
import retrofit2.Call
import retrofit2.http.*

//get service interface
interface GetService {
    @GET
    fun getUser(@Url s: String): Call<GetUserResponse>

}