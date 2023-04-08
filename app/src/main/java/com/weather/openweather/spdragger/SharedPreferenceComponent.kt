package com.weather.openweather.spdragger

import com.weather.openweather.mvvm.WeatherActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SharedPreferenceModule::class])
interface SharedPreferenceComponent {
    fun inject(weatherActivity: WeatherActivity?)
}