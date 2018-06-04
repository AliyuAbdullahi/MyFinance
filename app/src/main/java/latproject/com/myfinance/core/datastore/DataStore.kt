package latproject.com.myfinance.core.datastore

import android.content.Context
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.room.User
import latproject.com.myfinance.core.room.BankTransaction

class DataStore(context: Context) {
    private var offlineStore = OfflineStore(context)
    private var onlineStore = OnlineStore()

    fun saveUser(user: User, onUserAdded:(userAdded: User)-> Unit) {
        offlineStore.addUser(user)

        if(offlineStore.getUser() != null) {
            val savedUser = offlineStore.getUser()
            if(savedUser == user) {
                onUserAdded(savedUser)
            }
        }
    }

    fun getUser(): User? {
        return offlineStore.getUser()
    }

    fun getTransactionsForBank(bankName: String): List<BankTransaction>? {
        return offlineStore.getAllTransactionsForABank(bankName)
    }

    fun getAllTransactions(): List<BankTransaction>? {
        return offlineStore.getAllTransactions()
    }

    fun addTransaction(bankTransaction: BankTransaction,
                       onTransactionAddedListener: (transaction: BankTransaction) -> Unit) {
        offlineStore.addTransaction(bankTransaction)

        if(getAllTransactions() != null) {
            if(getAllTransactions()!!.any { it == bankTransaction }) {
                onTransactionAddedListener(bankTransaction)
            }
        }
    }

    fun addTransaction(bankTransaction: BankTransaction) {
        offlineStore.addTransaction(bankTransaction)
    }

    fun getBudgetsForABank(bankName: String): List<Budget>? {
        return offlineStore.getBudgetsForBank(bankName)
    }

    fun getBudgets(): List<Budget>? {
        return offlineStore.getBudgets()
    }

    fun deleteBudget(budget: Budget) {
        offlineStore.deleteBudget(budget)
    }

    fun transactionExist(bankTransaction: BankTransaction): Boolean {
        val transactions = getAllTransactions() ?: return false

        transactions.forEach { if(it == bankTransaction) return false }

        return true
    }
}