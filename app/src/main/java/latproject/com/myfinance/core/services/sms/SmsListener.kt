package latproject.com.myfinance.core.services.sms

import latproject.com.myfinance.core.model.SmsMessage

interface SmsListener {
    fun onMessageReceived(message: android.telephony.SmsMessage)
}