package latproject.com.myfinance.core.services.sms

interface SmsListener {
    fun onMessageReceived(message: String)
}