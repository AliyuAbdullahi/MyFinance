package latproject.com.myfinance.core.utils

import android.util.Log

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.String.Companion

object FormattingUtility {
    @Throws(ParseException::class)
    fun getDateFromCreatedAt(createdAt: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

        val returnedFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)

        val date = dateFormat.parse(createdAt)

        Log.d("getDateFromCreatedAt", returnedFormat.format(date))

        return returnedFormat.format(date)
    }

    fun getTimeFromUnixTimeStamp(unixTimeStamp: Long): String {
        val date = Date(unixTimeStamp)

        val dateFormatter = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
        Log.d("getTimeFromUnixTimeStam", dateFormatter.format(date))
        return dateFormatter.format(date)
    }

    fun getDateTime(timeStamp: Long): String {
        val date = Date(timeStamp)

        val dateFormatter = SimpleDateFormat("dd - MMM - yyyy HH:mm", Locale.ENGLISH)
        Log.d("getTimeFromUnixTimeStam", dateFormatter.format(date))
        return dateFormatter.format(date)
    }

}