package com.weather.openweather.spdragger

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPreferenceModule {
    private var context: Context? = null
    fun SharedPreferenceModule(context: Context?) {
        this.context = context
    }
    @Singleton
    @Provides
    fun provideContext(): Context? {
        return context
    }
    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context?): SharedPreferences? {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}