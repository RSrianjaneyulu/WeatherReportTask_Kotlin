package com.weather.openweather.mvvm

import com.weather.openweather.mvvm.modelclasses.CommonWeather
import com.weather.openweather.networkcalls.ServerUtil
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("weather?")
    suspend fun getWeatherReport(@Query("q") q : String, @Query("appid") appid : String): Response<CommonWeather>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null){
                val retrofit = Retrofit.Builder().baseUrl(ServerUtil.getWeatherDetailsURL).addConverterFactory(
                    GsonConverterFactory.create()).build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}