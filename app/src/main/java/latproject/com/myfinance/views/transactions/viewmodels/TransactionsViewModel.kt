package latproject.com.myfinance.views.transactions.viewmodels

import android.content.Context
import latproject.com.myfinance.core.model.SmsMessage
import latproject.com.myfinance.core.model.modelparser.TransactionParser
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.services.sms.SmsReader
import latproject.com.myfinance.core.view.CoreViewModel

class TransactionsViewModel(context: Context) : CoreViewModel(context) {

    fun getAllTransactionsForBank(bankName: String): List<RealmBankTransaction>? {
        return dataStore.getTransactionsForBank(bankName)
    }

    fun getBank(): String? {
        return dataStore.getBank()
    }

}