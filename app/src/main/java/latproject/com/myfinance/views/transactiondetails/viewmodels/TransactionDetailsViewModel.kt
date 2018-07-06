package latproject.com.myfinance.views.transactiondetails.viewmodels

import android.content.Context
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.view.CoreViewModel

class TransactionDetailsViewModel(context: Context): CoreViewModel(context) {
    fun transaction(bankTransactionId: String): RealmBankTransaction? {
        return dataStore.getAllTransactions()?.first { it.id == bankTransactionId }
    }

}