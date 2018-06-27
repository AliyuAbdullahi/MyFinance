package latproject.com.myfinance.core.view

import android.content.Context
import android.databinding.BaseObservable
import latproject.com.myfinance.core.datastore.DataStore
import latproject.com.myfinance.core.model.SmsMessage
import latproject.com.myfinance.core.model.modelparser.TransactionParser
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.services.sms.SmsReader

open class CoreViewModel(val context: Context): BaseObservable() {
    var dataStore = DataStore(context)

    fun saveRealmTransaction(realmBankTransaction: RealmBankTransaction) {
        dataStore.addRealmTransaction(realmBankTransaction)
    }

    fun getTransactions():List<RealmBankTransaction>? {
        return dataStore.getAllTransactions()
    }

    fun getTransactionsForBank(bankName: String): List<RealmBankTransaction>? {
        return getTransactions()?.filter { it.bank.toLowerCase().contains(bankName.toLowerCase().substring(0, bankName.length/2))}
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

    fun getBankName(): String? {
        return dataStore.getBank()
    }
}