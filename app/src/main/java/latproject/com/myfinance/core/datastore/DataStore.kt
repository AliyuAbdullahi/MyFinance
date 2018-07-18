package latproject.com.myfinance.core.datastore

import android.content.Context
import io.realm.Realm
import io.realm.RealmObject
import latproject.com.myfinance.core.model.OnboardingStatus
import latproject.com.myfinance.core.room.*

class DataStore(context: Context) {
    private var offlineStore = OfflineStore(context)
    private var onlineStore = OnlineStore()
    fun saveUser(user: User, onUserAdded: (userAdded: User) -> Unit) {
        offlineStore.addUser(user)

        if (offlineStore.getUser() != null) {
            val savedUser = offlineStore.getUser()
            if (savedUser == user) {
                onUserAdded(savedUser)
            }
        }
    }

    fun getUser(): User? {
        return offlineStore.getUser()
    }

    fun getTransactionsForBank(bankName: String): List<RealmBankTransaction>? {
        return offlineStore.getAllTransactionsForABank(bankName)
    }

    fun getAllTransactions(): List<RealmBankTransaction>? {
        return offlineStore.getAllTransactions()
    }

    fun addTransaction(bankTransaction: RealmBankTransaction,
                       onTransactionAddedListener: (transaction: RealmBankTransaction) -> Unit) {
        offlineStore.addTransaction(bankTransaction)

        if (getAllTransactions() != null) {
            if (getAllTransactions()!!.any { it.id == bankTransaction.id }) {
                onTransactionAddedListener(bankTransaction)
            }
        }
    }


    fun addTransaction(bankTransaction: RealmBankTransaction) {
        offlineStore.addTransaction(bankTransaction)
    }

    fun addTransaction(list: List<RealmBankTransaction>) {
        val oldTrx = offlineStore.getAllTransactions()
        offlineStore.save(list)
    }

    fun addRealmTransaction(bankTransaction: RealmBankTransaction) {
        val allTrx = offlineStore.getAllTransactions()
        val exist = allTrx?.find { it == bankTransaction }

        if (exist == null)
            offlineStore.addTransaction(bankTransaction)
    }

    fun getBudgetsForABank(bankName: String): List<Budget>? {
        return offlineStore.getBudgetsForBank(bankName)
    }

    fun createBudget(budget: Budget) {
        offlineStore.addBudget(budget)
    }

    fun getBudgets(): List<Budget>? {
        return offlineStore.getBudgets()
    }

    fun getBankTextColor(): String? {
        return offlineStore.getUser()?.bankTextColor
    }

    fun getBank(): String? {
        return offlineStore.getUser()?.bank
    }

    fun deleteBudget(budget: Budget) {
        offlineStore.deleteBudget(budget)
    }

    fun transactionExist(bankTransaction: RealmBankTransaction): Boolean {
        val transactions = getAllTransactions() ?: return false

        transactions.forEach { if (it.id == bankTransaction.id) return false }

        return true
    }

    fun saveBudget(budget: Budget) {
        offlineStore.save(budget)
    }

    fun getRealm(): Realm {
        return offlineStore.getLocalRealm()
    }

    fun save(onboardingStatus: RealmObject) {
        offlineStore.saveObject(onboardingStatus)
    }

    fun getOnboarding(): List<OnboardingStatus>? {
        return offlineStore.getAll(OnboardingStatus::class.java)
    }

}