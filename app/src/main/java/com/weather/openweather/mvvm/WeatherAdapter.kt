package com.weather.openweather.mvvm

import android.R
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weather.openweather.databinding.ItemWeatherAdapterBinding
import com.weather.openweather.helperclasses.Helper.Companion.convertUnixTimeToDateMonth
import com.weather.openweather.mvvm.modelclasses.CommonWeather
import com.weather.openweather.networkcalls.ServerUtil
import org.apache.commons.lang3.StringUtils
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

class WeatherAdapter : RecyclerView.Adapter<WeatherViewHolder>() {
    var weatherList = mutableListOf<List<CommonWeather>>()
    var weathList = mutableListOf<List<CommonWeather>>();
    var currentDateTime: String = ""
    var latitudeStr: String = ""
    var longitudeStr: String = ""
    var weatherIcon: String = ""
    fun setWeatherList(weather: CommonWeather){
        weathList = mutableListOf()
        weathList.add(listOf(weather))
        this.weatherList = weathList.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWeatherAdapterBinding.inflate(inflater, parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        try {
            val weatherData = weatherList[position]
            if (weatherData != null) {
                holder.binding.ivIWAWeatherIcon.isVisible = true
                holder.binding.ivIWASpeedIcon.isVisible = true
                holder.binding.ivIWAPressureIcon.isVisible = true
                if (weatherData.get(position).dt != null) {
                    currentDateTime = weatherData.get(position).dt.toString()
                    if (StringUtils.isNotEmpty(currentDateTime)) {
                        holder.binding.tvIWACurrentDateAndTime.setText(convertUnixTimeToDateMonth(currentDateTime))
                    } else {
                        holder.binding.tvIWACurrentDateAndTime.isVisible = false
                    }
                } else {
                    holder.binding.tvIWACurrentDateAndTime.isVisible = false
                }
                latitudeStr = weatherData.get(position).coord.lat.toString()
                longitudeStr = weatherData.get(position).coord.lon.toString()
                if (StringUtils.isNotEmpty(latitudeStr) && StringUtils.isNotEmpty(longitudeStr)) {
                    val latitude: Double? = latitudeStr.toDouble()
                    val longitude: Double? = longitudeStr.toDouble()
                    val geocoder = Geocoder(holder.itemView.context, Locale.getDefault())
                    var addresses: List<Address>? = null
                    try {
                        addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
                        val address: String = addresses!![0].getAddressLine(0)
                        val locality: String = addresses!![0].locality
                        val countryCode: String = addresses!![0].countryCode
                        //val countryName: String = addresses!![0].countryName
                        //val postalCode: String = addresses!![0].postalCode
                        holder.binding.tvIWACurrentCityName.setText("$locality, $countryCode")
                    } catch (ioException: IOException) {
                        ioException.printStackTrace()
                    }
                } else {
                    if (StringUtils.isNotEmpty(weatherData.get(position).name) && StringUtils.isNotEmpty(weatherData.get(position).sys.country)) {
                        holder.binding.tvIWACurrentCityName.setText(weatherData.get(position).name + ", " + weatherData.get(position).sys.country)
                    } else {
                        holder.binding.tvIWACurrentCityName.isVisible = false
                    }
                }
                if (weatherData.get(position).weather != null && !weatherData.get(position).weather.isEmpty()) {
                    for (weather in weatherData.get(position).weather) {
                        weatherIcon = weather.icon
                    }
                    if (StringUtils.isNotEmpty(weatherIcon)) {
                        Glide.with(holder.itemView.context)
                            .load(ServerUtil.weatherImageURL + "img/wn/" + weatherIcon + "@2x.png")
                            .into(holder.binding.ivIWAWeatherIcon)
                    } else {
                        holder.binding.ivIWAWeatherIcon.setImageResource(R.drawable.ic_lock_silent_mode_off)
                    }
                } else {
                    holder.binding.ivIWAWeatherIcon.setImageResource(R.drawable.ic_lock_silent_mode_off)
                }
                if (weatherData.get(position).main.temp != null) {
                    val fahrenheit: Double?
                    val celsius: Double?
                    fahrenheit = weatherData.get(position).main.temp
                    celsius = (fahrenheit - 32) * 5 / 9
                    val df = DecimalFormat("#.##")
                    holder.binding.tvIWATemperature.setText(df.format(celsius) + "" + "°C")
                } else {
                    holder.binding.tvIWATemperature.isVisible = false
                }
                if (weatherData.get(position).main.feels_like != null) {
                    val fahrenheit: Double?
                    val celsius: Double?
                    fahrenheit = weatherData.get(position).main.feels_like
                    celsius = (fahrenheit - 32) * 5 / 9
                    val df = DecimalFormat("#.##")
                    holder.binding.tvIWAFeesLikeData.setText("Feels like: " + df.format(celsius) + "" + "°C" + ".Clear sky. Light breeze")
                } else {
                    holder.binding.tvIWAFeesLikeData.isVisible = false
                }
                if (weatherData.get(position).wind.speed != null) {
                    holder.binding.tvIWASpeedDetails.setText(weatherData.get(position).wind.speed.toString() + "" + "m/s E")
                } else {
                    holder.binding.tvIWASpeedDetails.isVisible = false
                }
                if (weatherData.get(position).main.pressure != null) {
                    holder.binding.tvIWAPressureDetails.setText(weatherData.get(position).main.pressure.toString() + "" + "hPa")
                } else {
                    holder.binding.tvIWAPressureDetails.isVisible = false
                }
                if (weatherData.get(position).main.humidity != null) {
                    holder.binding.tvIWAHumidityDetails.setText("Humidity: " + weatherData.get(position).main.humidity + "" + "%")
                } else {
                    holder.binding.tvIWAHumidityDetails.isVisible = false
                }
                if (weatherData.get(position).sys.type != null) {
                    holder.binding.tvIWAUvDetails.setText("UV: " + weatherData.get(position).sys.type + "")
                } else {
                    holder.binding.tvIWAUvDetails.isVisible = false
                }
                if (weatherData.get(position).clouds.all != null) {
                    val fahren: String = weatherData.get(position).clouds.all.toString()
                    val fahrenheit: Double?
                    val celsius: Double?
                    fahrenheit = fahren.toDouble()
                    celsius = (fahrenheit - 32) * 5 / 9
                    val df = DecimalFormat("#.##")
                    holder.binding.tvIWADewPointDetails.setText("Dew point: " + df.format(celsius) + "" + "°C")
                } else {
                    holder.binding.tvIWADewPointDetails.isVisible = false
                }
                if (weatherData.get(position).visibility != null) {
                    val kms: String = weatherData.get(position).visibility.toString()
                    val kilometers = kms.toDouble() / 1000
                    val df = DecimalFormat("#.##")
                    holder.binding.tvIWAVisibilityData.setText("Visibility: " + df.format(kilometers) + "" + "km")
                } else {
                    holder.binding.tvIWAVisibilityData.isVisible = false
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }
}
class WeatherViewHolder(val binding: ItemWeatherAdapterBinding) : RecyclerView.ViewHolder(binding.root) {}