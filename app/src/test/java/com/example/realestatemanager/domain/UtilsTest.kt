package com.example.realestatemanager.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

class UtilsTest {

    private val sumToConvert = 100

    @Test
    fun convert_dollars_to_euros() {
        val sumInDollars = Utils.convertDollarToEuro(sumToConvert)
        val correctReturn = (Constant.DOLLARS_TO_EURO * sumToConvert).roundToInt()
        assertThat(sumInDollars).isEqualTo(correctReturn)
    }

    @Test
    fun convert_dollars_to_euro() {
        val sumInEuro = Utils.convertEuroToDollar(sumToConvert)
        val correctReturn = (Constant.EURO_TO_DOLLARS * sumToConvert).roundToInt()
        assertThat(sumInEuro).isEqualTo(correctReturn)
    }

    @Test
    fun return_date_to_ddMMyyyy_format() {
        val dateOfDay = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT, Locale.getDefault())
        val wrongFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateOfDay.format(formatter)
        val wrongFormattedDate = dateOfDay.format(wrongFormatter)
        val dateToCheck = Utils.todayDate

        assertThat(dateToCheck).isEqualTo(formattedDate)
        assertThat(dateToCheck).isNotEqualTo(wrongFormattedDate)
    }
}