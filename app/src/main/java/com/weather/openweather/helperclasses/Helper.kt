package com.weather.openweather.helperclasses

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    companion object {
        private var mContext: Context? = null
        private val df: SimpleDateFormat? = null

        fun Helper(context: Context?) {
            mContext = context
        }
        //Toast Message
        fun showShortToast(ctx: Context?, message: String?) {
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
        }

        //Convert Capital Letter
        fun capitalize(str: String?): String? {
            return if (str == null || str.isEmpty()) {
                str
            } else str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
        }

        //Convert Date Format
        fun convertUnixTimeToDateMonth(unixDateTime: String?): String? {
            var amPm = ""
            val sdf = SimpleDateFormat("MMM dd,  hh:mm")
            val millis = java.lang.Long.valueOf(unixDateTime) * 1000
            val date = Date()
            date.time = millis
            val sdf1 = SimpleDateFormat("aa")
            //converting date format for US
            val timezoneInAmerica = TimeZone.getTimeZone("America/New_York")
            sdf1.timeZone = timezoneInAmerica
            amPm = if (sdf1.format(date).equals("pm", ignoreCase = true)) {
                "pm"
            } else {
                "am"
            }
            return sdf.format(date) + amPm
        }

        //Chcek internet connection
        fun checkInternetConnection(context: Context): Boolean {
            val connMgr =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connMgr.activeNetworkInfo
            if (activeNetworkInfo != null) { // connected to the internet
                showShortToast(context, activeNetworkInfo.typeName);
                return if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    true
                } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                    true
                } else {
                    false
                }
            } else {
                showShortToast(context, "Internet Connection is Required");
            }
            return false
        }
    }
}