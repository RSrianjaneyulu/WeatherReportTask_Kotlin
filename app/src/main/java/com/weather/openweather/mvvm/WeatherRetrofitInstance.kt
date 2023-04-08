package com.weather.openweather.mvvm

import com.weather.openweather.networkcalls.ServerUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRetrofitInstance {
    val weatherApi : CommonWeatherApi by lazy {
        Retrofit.Builder().baseUrl(ServerUtil.getWeatherDetailsURL).addConverterFactory(
            GsonConverterFactory.create()).build().create(CommonWeatherApi::class.java)
    }
}