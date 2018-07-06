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

    fun getBankName(): String? {
        return dataStore.getBank()
    }
}