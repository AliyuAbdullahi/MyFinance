package latproject.com.myfinance.views.transactions.viewmodels

import android.content.Context
import latproject.com.myfinance.core.model.SmsMessage
import latproject.com.myfinance.core.model.modelparser.TransactionParser
import latproject.com.myfinance.core.services.sms.SmsReader
import latproject.com.myfinance.core.view.CoreViewModel
import latproject.com.myfinance.core.room.BankTransaction

class TransactionsViewModel(val context: Context) : CoreViewModel(context) {

    fun getAllTransactionsForBank(bankName: String): List<BankTransaction>? {
        return dataStore.getTransactionsForBank(bankName)
    }

    private fun getMessages(bankName: String): List<SmsMessage> {
        val smsReader = SmsReader(context)

        return smsReader.getMessage().filter { it.from.toLowerCase().contains(bankName.toLowerCase().substring(0, bankName.length/2)) }
    }

    fun getAllMessages(): List<SmsMessage> {
        val smsReader = SmsReader(context)

        return smsReader.getMessage()
    }

    fun getBank() : String?  {
        return dataStore.getBank()
    }

    fun loadTransactions(bankName: String):MutableList<BankTransaction?> {
        val bankTransactions = mutableListOf<BankTransaction?>()

        getMessages(bankName).forEach {
            bankTransactions.add(TransactionParser.parseToTransaction(bankName, it))
        }

        return bankTransactions
    }
}