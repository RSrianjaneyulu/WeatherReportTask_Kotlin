package com.weather.openweather.mvvm.modelclasses

data class CommonWeather(val coord: Coordinates, val weather: List<Weather>, val base: String, val main: Main, val visibility: Long, val wind: Wind, val clouds: Cloud, val dt: Long, val sys: Sys, val timezone: Long, val id: Long, val name: String, val cod: String)
