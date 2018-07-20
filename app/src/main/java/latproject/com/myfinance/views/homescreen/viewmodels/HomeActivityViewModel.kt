package latproject.com.myfinance.views.homescreen.viewmodels

import android.content.Context
import android.databinding.Bindable
import latproject.com.myfinance.BR
import latproject.com.myfinance.core.model.Bank
import latproject.com.myfinance.core.model.SmsMessage
import latproject.com.myfinance.core.model.modelparser.TransactionParser
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.services.sms.SmsReader
import latproject.com.myfinance.core.view.CoreViewModel

class HomeActivityViewModel(context: Context) : CoreViewModel(context) {

    @Bindable
    var loading: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }

    fun getBudgets(): List<Budget>? {
        val bankName = getBankName()
        return dataStore.getBudgets()?.filter { it.bank == bankName }
    }

    fun getBudgetsBy(bankName: String): List<Budget>? {
        return dataStore.getBudgetsForABank(bankName)
    }

    fun currentBank(): Bank? {
        var bank = Bank()
        var user = dataStore.getUser()
        if (user != null) {
            bank.textColor = user.bankTextColor
            bank.backgroundColor = user.bankBackGroundColor
            bank.name = user.bank

            return bank
        }

        return null
    }

    fun loadTransactionsUnderTheHood(): List<SmsMessage> {
        val bankName = dataStore.getBank()
        if (bankName != null) {
            return getMessages("union")
        }
        return listOf()
    }

    fun getMessages(bankName: String): List<SmsMessage> {
        val smsReader = SmsReader(context)

        return smsReader.getMessage().filter { it.from.toLowerCase().contains(bankName.toLowerCase()) }
    }


    fun loadTransactions(bankName: String): MutableList<RealmBankTransaction?> {
        val bankTransactions = mutableListOf<RealmBankTransaction?>()

        getMessages(bankName).forEach {
            val currentTransaction = TransactionParser.parseToTransaction(bankName, it)
            if (currentTransaction != null) {
                bankTransactions.add(currentTransaction)
                if (currentTransaction.amount != 0.0)
                    dataStore.addTransaction(currentTransaction)
            }
        }

        return bankTransactions
    }

    fun saveRealmTransaction(realmBankTransaction: RealmBankTransaction,
                             callback: (saved: Boolean?, realmBankTransaction: RealmBankTransaction?) -> Unit) {
        dataStore.addRealmTransaction(realmBankTransaction)

        dataStore.getAllTransactions()?.forEach {
            if (it.id == realmBankTransaction.id) {
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