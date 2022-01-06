package com.example.karigorit.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

// check network is available or not
fun Context.isNetworkAvailable(ctxContext: Context): Boolean {
    val connectivityManager = ctxContext
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!
        .isConnected || connectivityManager.getNetworkInfo(
        ConnectivityManager.TYPE_MOBILE
    )!!.isConnected
}
