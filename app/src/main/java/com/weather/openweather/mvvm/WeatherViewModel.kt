package com.weather.openweather.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weather.openweather.mvvm.modelclasses.CommonWeather
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val weatherList = MutableLiveData<CommonWeather>()
    var job: Job? = null
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    //Using Coroutines
    fun getWeatherReport(cityName: String, appid: String) {
        job = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val response = RetrofitService.getInstance().getWeatherReport(cityName, appid)
            withContext(Dispatchers.Main) {
                try {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                weatherList.postValue(response.body()!!)
                                loading.value = false
                            }else {
                                return@withContext
                            }
                        } else {
                            onError("Error : ${response.message()}")
                        }
                    }
                }catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    //Optional - Using Retrofit enqueue, Callback
    fun getWeatherReportData(cityName: String, appid: String) {
        WeatherRetrofitInstance.weatherApi.getWeatherReport(cityName, appid).enqueue(object :
            Callback<CommonWeather> {
            override fun onResponse(call: Call<CommonWeather>, response: Response<CommonWeather>) {
                try {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                weatherList.postValue(response.body()!!)
                                loading.value = false
                            } else {
                                return
                            }
                        } else {
                            onError("Error : ${response.message()}")
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            override fun onFailure(call: Call<CommonWeather>, t: Throwable) {
                errorMessage.postValue(t.message)
                loading.value = false
            }

        })
    }
    //Error message
    private fun onError(message: String){
        errorMessage.value = message
        loading.value = false
    }
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
    //observe the live data with the help of the Observer() function
    fun observeWeatherLiveData() : LiveData<CommonWeather> {
        return weatherList
    }
}