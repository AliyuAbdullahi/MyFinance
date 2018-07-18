package latproject.com.myfinance.core.services.sms

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.telephony.SmsMessage
import latproject.com.myfinance.core.datastore.DataStore
import latproject.com.myfinance.core.globals.Constants
import latproject.com.myfinance.core.model.Transactions
import latproject.com.myfinance.core.model.modelparser.TransactionParser
import timber.log.Timber

class SmsService : IntentService(Constants.SMS_SERVICE_NAME), SmsListener {
    lateinit var dataStore: DataStore

    inner class ServiceBinder : Binder() {
        val service: SmsService
            get() = this@SmsService
    }

    override fun onMessageReceived(message: SmsMessage) {
        try{
            val bankName = requireNotNull(dataStore.getBank())

            if (message.displayOriginatingAddress.toLowerCase().contains(bankName.toLowerCase().substring(0, bankName.length / 2))) {

                val sms = latproject.com.myfinance.core.model.SmsMessage()
                sms.body = message.displayMessageBody
                sms.from = message.displayOriginatingAddress
                sms.date = message.timestampMillis

                val bankTransaction = TransactionParser.parseToTransaction(bankName, sms)

                if (bankTransaction != null) {
                    dataStore.addRealmTransaction(bankTransaction)
                }
            }
        }catch (ex: IllegalStateException) {
            Timber.e("ERROR %s", ex)
        }
    }

    override fun onCreate() {
        super.onCreate()
        dataStore = DataStore(this)
    }

    private val binder = ServiceBinder()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onHandleIntent(p0: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        readMessages()
        SmsReceiver.bindListener(this)
        return Service.START_STICKY
    }

    fun readMessages() {
        val smsReader = SmsReader(this)
        val bankName = dataStore.getBank()
        if (bankName != null) {
            smsReader.getMessage().forEach {
                val currentTransaction = TransactionParser.parseToTransaction(bankName, it)
                if (currentTransaction != null) {
                    if (currentTransaction.amount != 0.0) {
                        dataStore.addTransaction(currentTransaction)
                    }
                }
            }

            Handler().postDelayed({
                val transactions = Transactions()
                transactions.transactions = dataStore.getTransactionsForBank(bankName)
                val intent = Intent(Constants.SMS_TRANSACTION_LIST_INTENT)
                intent.putExtra(Constants.TRANSACTION_LIST, transactions)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }, 20)
        }
    }
}