package latproject.com.myfinance.core.datastore

import android.arch.persistence.room.Room
import android.content.Context
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.room.User
import latproject.com.myfinance.core.room.BankTransaction
import latproject.com.myfinance.core.room.BankTransactionDataBase

class OfflineStore(context: Context) {
    var db: BankTransactionDataBase? = null
    init {
        db = Room.databaseBuilder(context, BankTransactionDataBase::class.java, "finance_db")
                .allowMainThreadQueries().build()
    }

    fun addUser(user: User) {
        val currentUser = db?.getUserDataAccess()?.getUser()
        if(currentUser != null && currentUser != user) {
            db?.getUserDataAccess()?.deleteUser(currentUser)

            db?.getUserDataAccess()?.addUser(user)
        } else {
            db?.getUserDataAccess()?.addUser(user)
        }
    }

    fun updateUser(user: User) {
        db?.getUserDataAccess()?.updateUser(user)
    }

    fun addTransaction(transaction: BankTransaction) {
        val allTransactions = db?.getTransactionsDataAccess()?.getTransactions()

        if(allTransactions != null) {
            if(!allTransactions.any { it == transaction }) {
                db?.getTransactionsDataAccess()?.addTransaction(transaction)
            }
        }
    }

    fun getUser():User? {
        return db?.getUserDataAccess()?.getUser()
    }

    fun getAllTransactionsForABank(bankName: String): List<BankTransaction>? {
        return db?.getTransactionsDataAccess()?.getTransactionsForBank(bankName)
    }

    fun getAllTransactions(): List<BankTransaction>? {
        return db?.getTransactionsDataAccess()?.getTransactions()
    }

    fun getBudgets(): List<Budget>? {
        return db?.getBudgetDataAccess()?.getAllBudgets()
    }

    fun getBudgetsForBank(bankName: String): List<Budget>? {
        return db?.getBudgetDataAccess()?.getBudgetForABank(bankName)
    }

    fun deleteBudget(budget: Budget) {
        db?.getBudgetDataAccess()?.deleteBudget(budget)
    }

    fun addBudget(budget: Budget) {
        db?.getBudgetDataAccess()?.createBudget(budget)
    }
}