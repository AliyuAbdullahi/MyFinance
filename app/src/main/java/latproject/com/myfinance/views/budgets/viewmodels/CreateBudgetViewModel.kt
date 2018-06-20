package latproject.com.myfinance.views.budgets.viewmodels

import android.content.Context
import android.databinding.Bindable
import latproject.com.myfinance.BR
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.view.CoreViewModel

class CreateBudgetViewModel(context: Context) : CoreViewModel(context) {

    fun getBaseBalance(): Double {
        val currentBank = dataStore.getBank()
        val base = dataStore.getTransactionsForBank(currentBank!!)?.map { it.balanceAfterTransaction }?.last()

        return base ?: 0.00
    }

    @Bindable
    var currentBank: String = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.currentBank)
    }
    get() = getBank()?:""

    fun getBank(): String? {
        return dataStore.getBank()
    }

    fun createBudget(budget: Budget, onBudgetCreated: (budget: Budget?) -> Unit) {
        dataStore.createBudget(budget)

        val allBudgets = dataStore.getBudgets()

        onBudgetCreated(allBudgets?.first { it.id ==budget.id })
    }
}