package latproject.com.myfinance.core.model

class SmsMessage {
    var from: String = ""
    var body: String = ""
    var date: Long = 0L
    override fun toString(): String {
        return "SmsMessage(from='$from', body='$body', date='$date')"
    }
}