package com.example.karigorit

import com.google.gson.annotations.SerializedName
import java.util.*


data class GetUserResponse (

        @SerializedName("results" ) var results : ArrayList<Results> = ArrayList(),
        @SerializedName("info"    ) var info    : Info?              = Info()

)