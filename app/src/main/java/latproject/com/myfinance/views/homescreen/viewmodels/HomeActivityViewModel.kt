package latproject.com.myfinance.views.homescreen.viewmodels

import android.content.Context
import latproject.com.myfinance.core.model.SmsMessage
import latproject.com.myfinance.core.model.modelparser.TransactionParser
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.services.sms.SmsReader
import latproject.com.myfinance.core.view.CoreViewModel

class HomeActivityViewModel (context: Context): CoreViewModel(context) {

    fun getBudgets(): List<Budget>? {
       return dataStore.getBudgets()
    }

    fun getBudgetsBy(bankName: String): List<Budget>? {
        return dataStore.getBudgetsForABank(bankName)
    }

    fun loadTransactionsUnderTheHood() {
        val bankName = dataStore.getBank()
        if(bankName != null) {
            getMessages(bankName).forEach {
                val currentTransaction = TransactionParser.parseToTransaction(bankName, it)
                if(currentTransaction != null) {
                    dataStore.addTransaction(currentTransaction)
                }
            }
        }
    }

    fun getMessages(bankName: String): List<SmsMessage> {
        val smsReader = SmsReader(context)

        return smsReader.getMessage().filter { it.from.toLowerCase().contains(bankName.toLowerCase().substring(0, bankName.length/2)) }
    }


    fun loadTransactions(bankName: String):MutableList<RealmBankTransaction?> {
        val bankTransactions = mutableListOf<RealmBankTransaction?>()

        getMessages(bankName).forEach {
            val currentTransaction = TransactionParser.parseToTransaction(bankName, it)
            if(currentTransaction != null) {
                bankTransactions.add(currentTransaction)
                dataStore.addTransaction(currentTransaction)
            }
        }

        return bankTransactions
    }

    fun saveRealmTransaction(realmBankTransaction: RealmBankTransaction,
                             callback: (saved: Boolean?, realmBankTransaction: RealmBankTransaction?) -> Unit) {
        dataStore.addRealmTransaction(realmBankTransaction)

        dataStore.getAllTransactions()?.forEach {
            if(it.id == realmBankTransaction.id) {
                callback(true, realmBankTransaction)
            } else {
                callback(false, null)
            }
        }
    }

    fun saveRealmTransaction(list: List<RealmBankTransaction>) {
        dataStore.addTransaction(list)
    }
}