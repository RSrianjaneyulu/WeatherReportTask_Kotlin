package com.weather.openweather

import com.google.common.truth.Truth.assertThat
import com.weather.openweather.unittest.SearchUtil
import org.junit.Test

class SearchUtilTest {

    @Test
    fun `empty cityname returns false`(){
        // Pass the value to the function of SearchUtil class
        val result = SearchUtil.validCityNameInput("")
        // assertThat() comes from the truth library that we added earlier
        // put result in it and assign the boolean that it should return
        assertThat(result).isFalse()
    }
    // in this test cityname already taken returns false
    @Test
    fun `cityname already taken returns false`() {
        val result = SearchUtil.validCityNameInput(
            "Phoenix")
        assertThat(result).isFalse()
    }
    // in this test if cityname has less than three characters than return false
    @Test
    fun `less than three characters cityname return false`() {
        val result = SearchUtil.validCityNameInput(
            "Chicago"
        )
        assertThat(result).isFalse()
    }
}