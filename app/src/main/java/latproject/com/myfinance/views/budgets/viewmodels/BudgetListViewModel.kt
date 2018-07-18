package latproject.com.myfinance.views.budgets.viewmodels

import android.content.Context
import io.realm.Realm
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.view.CoreViewModel

class BudgetListViewModel(context: Context): CoreViewModel(context) {

    fun getBudgets(): List<Budget>? {
        val bankName = dataStore.getBank()
        return dataStore.getBudgets()?.filter { it.bank == bankName }
    }

    fun saveBudget(budget: Budget) {
        dataStore.saveBudget(budget)
    }

    fun deleteBudget(budget: Budget) {
        dataStore.deleteBudget(budget)
    }

    fun getRealm(): Realm {
        return dataStore.getRealm()
    }
}
