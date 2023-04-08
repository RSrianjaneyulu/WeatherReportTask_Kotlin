package com.weather.openweather.spdragger

import android.app.Activity
import android.content.Context

object SPHelper {
    const val spName = "weather"
    var cityName = "cityName"
    fun saveStringData(act: Activity, key: String?, value: String?) {
        val pref = act.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.commit()
    }
    fun getStringData(act: Activity, key: String?, value: String?): String? {
        val pref = act.getSharedPreferences(spName, Context.MODE_PRIVATE)
        return pref.getString(key, value)
    }

}