package latproject.com.myfinance.core.services.sms

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import latproject.com.myfinance.core.model.SmsMessage

class SmsReader(val context: Context) {
    var SORT_ORDER = "date DESC"

    @SuppressLint("Recycle")
    fun getMessage(): List<SmsMessage> {
        val smsList = mutableListOf<SmsMessage>()
        val cursor = context.contentResolver.query(Uri.parse("content://sms/inbox"),
                null, null, null,
                SORT_ORDER)

        if (cursor.moveToFirst()) {
            do {

                val address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address"))
                val body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

                val smsMessage = SmsMessage()

                smsMessage.from = address
                smsMessage.body = body
                smsMessage.date = date.toLong()

                smsList.add(smsMessage)
            } while (cursor.moveToNext())
        }

        return smsList
    }
}