package com.weather.openweather.mvvm

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.weather.openweather.R
import com.weather.openweather.databinding.ActivityWeatherBinding
import com.weather.openweather.helperclasses.Helper
import com.weather.openweather.spdragger.SPHelper
import com.weather.openweather.spdragger.SPHelper.getStringData
import com.weather.openweather.spdragger.SharedPreferenceComponent
import org.apache.commons.lang3.StringUtils
import java.util.*
import javax.inject.Inject

class WeatherActivity : AppCompatActivity(){

    private val citiesList = arrayOf(
        "New York",
        "Chicago",
        "Boston",
        "San Diego",
        "Los Angeles",
        "San Francisco",
        "Philadelphia",
        "Austin",
        "Seattle",
        "Nashville",
        "Phoenix",
        "San Jose",
        "San Antonio",
        "Oklahoma City",
        "Las Vegas",
        "Baltimore",
        "Houston",
        "New Orleans",
        "Washington",
        "Washington, D.C.",
        "Columbus",
        "Dallas",
        "Indianapolis",
        "Atlanta",
        "Detroit",
        "Denver",
        "Honolulu",
        "Jacksonville",
        "Fort Worth",
        "El Paso",
        "Kansas City",
        "Milwaukee",
        "Memphis",
        "Sacramento",
        "Charlotte",
        "Portland",
        "Fresno",
        "Raleigh",
        "Albuquerque",
        "Tucson",
        "Colorado Springs",
        "Louisville",
        "Miami",
        "Tulsa",
        "Wichita",
        "Omaha",
        "Virginia Beach",
        "Salt Lake City",
        "Oakland",
        "Des Moines",
        "Charleston",
        "Mesa",
        "California",
        "Texas",
        "Florida",
        "Indiana",
        "Michigan",
        "Missouri",
        "New Jersey",
        "St. Petersburg",
        "Moreno Valley",
        "Tacoma",
        "Rochester",
        "Columbus",
        "Frisco",
        "Oxnard",
        "Sioux Falls",
        "Tallahassee",
        "Virginia",
        "Louisiana",
        "Illinois",
        "Aurora",
        "Santa Rosa",
        "Lancaster",
        "Springfield",
        "Hayward",
        "Clarksville",
        "Paterson",
        "Hollywood",
        "Mississippi",
        "Rockford",
        "Fullerton",
        "West Valley City",
        "Elizabeth",
        "Kent",
        "Miramar",
        "Midland",
        "Iowa",
        "Carrollton",
        "Fargo",
        "Pearland",
        "North Dakota",
        "Thousand Oaks",
        "Allentown",
        "Colorado",
        "Clovis",
        "Hartford",
        "Connecticut",
        "Wilmington",
        "Fairfield",
        "Cambridge",
        "Billings",
        "West Palm Beach",
        "Westminster",
        "Provo",
        "Lewisville",
        "New Mexico",
        "Odessa",
        "Nevada",
        "Greeley",
        "Tyler",
        "Oregon",
        "Rio Rancho",
        "Massachusetts",
        "New Bedford",
        "Longmont",
        "Hesperia",
        "Chico",
        "Burbank",
        "Murfreesboro",
        "Kansas",
        "Idaho",
        "Pittsburgh",
        "Detroit",
        "Pennsylvania",
        "Jacksonville",
        "Indianapolis",
        "Charlotte",
        "Jefferson City",
        "Arizona",
        "Ohio",
        "North Carolina",
        "Oklahoma",
        "Denver",
        "El Paso",
        "Kentucky",
        "Nevada",
        "Milwaukee",
        "Baltimore",
        "Wisconsin",
        "New Mexico",
        "Fresno",
        "Tucson",
        "Sacramento",
        "Nebraska",
        "Omaha",
        "Raleigh",
        "Oakland",
        "Miami",
        "Minneapolis",
        "Bakersfield",
        "Tampa",
        "New Orleans",
        "Honolulu",
        "Lexington",
        "Stockton",
        "St. Louis",
        "Lubbock",
        "Buffalo",
        "Glendale",
        "Hialeah",
        "Connecticut"
    )
    var cityName: String = ""
    var cityDetails: String = ""
    var appId: String = "4db19b747b0a3369815069fb2ef8d024"
    //var appId: String = "6134f58493547c1c8a615102693ac120"
    private var cityList: MutableList<String>? = null
    private var cityDataList: MutableList<String>? = null
    lateinit var weatherViewModel: WeatherViewModel
    private val weatherAdapter = WeatherAdapter()
    lateinit var weatherBinding: ActivityWeatherBinding
    private val sharedPreferenceComponent: SharedPreferenceComponent? = null
    @Inject
    var sharedPreferences: SharedPreferences? = null
    private val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherBinding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(weatherBinding.root)
        if(citiesList != null) {
            cityDataList = citiesList.toMutableList()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val lowercaseList = cityDataList!!.map { it.lowercase() }
                cityList = lowercaseList.toMutableList()
            }
        }
        //Location Enable Permission
        try {
            if (ContextCompat.checkSelfPermission(this@WeatherActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) !==
                PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this@WeatherActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this@WeatherActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                } else {
                    ActivityCompat.requestPermissions(this@WeatherActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
        //Search CityName
        weatherBinding.etAWEnterCityName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(text: CharSequence, start: Int,
                                       before: Int, count: Int) {
                cityName = text.toString().trim();
                if(StringUtils.isNotEmpty(cityName)){
                    weatherBinding.tvAWCityNameLabel.isVisible = true
                }else {
                    weatherBinding.tvAWCityNameLabel.isVisible = false
                }
            }
        })
        val timerThread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    try {
                        if (getStringData(this@WeatherActivity, SPHelper.cityName, "")!!.trim { it <= ' ' }.length > 0) {
                            runOnUiThread {
                                if (Helper.checkInternetConnection(this@WeatherActivity)) {
                                    cityName = getStringData(this@WeatherActivity, SPHelper.cityName, "")!!.trim { it <= ' ' }
                                    passingDataToServerCall()
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        }
        timerThread.start()
        //Search Icon Click
        weatherBinding.ivAWCityNameSearchIcon.setOnClickListener({
            cityNameValidationCheck()
        })
    }
    override fun onResume() {
        super.onResume()
    }
    //cityname validation check
    fun cityNameValidationCheck() {
        cityName = weatherBinding.etAWEnterCityName.getText().toString().trim().toLowerCase()
        if (!StringUtils.isNotEmpty(cityName)) {
            weatherBinding.etAWEnterCityName.requestFocus()
            weatherBinding.etAWEnterCityName.setError(getString(R.string.cityname_required))
            return
        }else if (cityName.length < 3){
            weatherBinding.etAWEnterCityName.requestFocus()
            weatherBinding.etAWEnterCityName.setError(getString(R.string.valid_citynamecharacters))
            return
        } else {
            weatherBinding.etAWEnterCityName.setError(null)
        }
        if (Helper.checkInternetConnection(this@WeatherActivity)) {
            if (StringUtils.isNotEmpty(cityName)) {
                cityDetails = if (cityName.contains(",")) {
                    val cityInfo = cityName.split(",").toTypedArray()
                    cityInfo[0].trim { it <= ' ' }.lowercase(Locale.getDefault())
                } else {
                    cityName
                }
                if (cityList!!.contains(cityDetails)) {
                    SPHelper.saveStringData(this@WeatherActivity, SPHelper.cityName, cityName);
                    passingDataToServerCall()
                } else {
                    Helper.showShortToast(
                        this@WeatherActivity, getString(R.string.valid_cityname))
                    weatherBinding.etAWEnterCityName.setText("")
                    weatherBinding.tvAWNoWeatherList.isVisible = true
                    weatherBinding.rvAWWeatherList.isVisible = false
                }
            }
        }
    }
    //Passing data to API call
    fun passingDataToServerCall(){
        weatherBinding.pbAWProgressDialog.visibility = View.VISIBLE
        weatherBinding.rvAWWeatherList.adapter = weatherAdapter
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        //Calling Coroutines function
        weatherViewModel.getWeatherReport(cityName, appId)
        //Calling Retrofit enqueue, Callback - Optional (if you use this, disable(comment) the above line and enable(un comment) the below line
        //weatherViewModel.getWeatherReportData(cityName, appId)
        weatherViewModel.observeWeatherLiveData().observe(this, Observer {
            weatherAdapter.setWeatherList(it)
        })
        weatherViewModel.errorMessage.observe(this, Observer {
            Helper.showShortToast(this, it)
        })
        weatherViewModel.loading.observe(this, Observer {
            if (it){
                weatherBinding.pbAWProgressDialog.visibility = View.VISIBLE
            }else {
                weatherBinding.pbAWProgressDialog.visibility= View.GONE
            }
        })
    }
}