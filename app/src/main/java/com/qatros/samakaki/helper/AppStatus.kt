package com.qatros.samakaki.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log


class AppStatus {
    var connectivityManager: ConnectivityManager? = null
    var wifiInfo: NetworkInfo? = null
    var mobileInfo: NetworkInfo? = null
    var connected = false
    val isOnline: Boolean
        get() {
            try {
                connectivityManager = context
                    ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                val networkInfo = connectivityManager!!.activeNetworkInfo
                connected = networkInfo != null && networkInfo.isAvailable &&
                        networkInfo.isConnected
                return connected
            } catch (e: Exception) {
                println("CheckConnectivity Exception: " + e.message)
                Log.v("connectivity", e.toString())
            }
            return connected
        }

    companion object {
        private val instance = AppStatus()
        var context: Context? = null
        fun getInstance(ctx: Context): AppStatus {
            context = ctx.getApplicationContext()
            return instance
        }
    }
}