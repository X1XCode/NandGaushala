package com.gaushala.utils

import java.text.SimpleDateFormat
import java.util.*


object Utils {
    fun getCurrentTimeStamp(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat(INPUT_CHECK_DATE_FORMAT)
        return df.format(c.time)
    }

    fun getShift() : Int{
        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)
        when (timeOfDay) {
            in 0..11 -> {
                return 0
            }
            in 12..15 -> {
                return 0
            }
            in 16..20 -> {
                return 1
            }
            in 21..23 -> {
                return 1
            }
        }
        return -1
    }


}