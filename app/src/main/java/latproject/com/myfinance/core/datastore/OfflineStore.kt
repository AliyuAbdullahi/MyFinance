package latproject.com.myfinance.core.datastore

import android.content.Context
import io.realm.Realm
import latproject.com.myfinance.core.datastore.realm.RealmManager
import latproject.com.myfinance.core.model.modelparser.ModelMapper
import latproject.com.myfinance.core.room.*

class OfflineStore(context: Context) {
    val realmManager = RealmManager(Realm.getDefaultInstance())

    fun addUser(user: User) {
        realmManager.save(user)
    }

    fun updateUser(user: User) {
        realmManager.saveOrUpdate(user)
    }

    fun addTransaction(realmBankTransaction: RealmBankTransaction) {
        realmManager.saveOrUpdate(realmBankTransaction)
    }

    fun getUser(): User? {
        return realmManager.findOne(User::class.java)
    }

    fun getAllTransactionsForABank(bankName: String): List<RealmBankTransaction>? {
        val allTransactions = realmManager.findAllByClass(RealmBankTransaction::class.java)

        return allTransactions.filter { it.bank.toLowerCase().contains(bankName.substring(0, bankName.length/2).toLowerCase()) }
    }

    fun getAllTransactions(): List<RealmBankTransaction>? {
        return realmManager.findAllByClass(RealmBankTransaction::class.java)
    }

    fun getBudgets(): List<Budget>? {
        return realmManager.findAllByClass(Budget::class.java)
    }

    fun getBudgetsForBank(bankName: String): List<Budget>? {
        return realmManager.findAllByClass(Budget::class.java).filter { it.bank == bankName }
    }

    fun deleteBudget(budget: Budget, onBudgetDeleted: (deleted: Boolean) -> Unit) {
        val budgetId = budget.id
        val budgets = realmManager.findAllByClass(Budget::class.java)
        val budgetToDelete = budgets.firstOrNull { it.id == budget.id }
        if (budgetToDelete != null)
            realmManager.delete(budgetToDelete)

        val all = realmManager.findAllByClass(Budget::class.java)
        val searchParam = all.find { it.id == budgetId }

        if (searchParam == null) {
            onBudgetDeleted(true)
        } else {
            onBudgetDeleted(false)
        }
    }

    fun deleteBudget(budget: Budget) {
        val budgets = realmManager.findAllByClass(Budget::class.java)
        val budgetToDelete = budgets.firstOrNull { it.id == budget.id }
        if (budgetToDelete != null)
            realmManager.delete(budgetToDelete)
    }

    fun dropAllTables() {
        realmManager.deleteAll()
    }

    fun addBudget(budget: Budget) {
        realmManager.saveOrUpdate(budget)
    }

    fun save(list: List<RealmBankTransaction>) {
        realmManager.saveOrUpdate(list)
    }

    fun getLocalRealm(): Realm {
        return realmManager.getRealm()
    }

    fun save(budget: Budget) {
        realmManager.saveOrUpdate(budget)
    }
}