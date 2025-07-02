package com.androit.monthandyearpicker

import android.content.Context
import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

object DateHelper {

    fun getMonthName(month: Int, monthFormat: String?, locale: Locale?): String {
        val formatter: DateFormat = SimpleDateFormat(monthFormat, locale)
        val calendar = GregorianCalendar()
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar[Calendar.MONTH] = month
        return formatter.format(calendar.time)
    }

    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }
}