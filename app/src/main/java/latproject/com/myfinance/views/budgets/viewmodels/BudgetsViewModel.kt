package latproject.com.myfinance.views.budgets.viewmodels

import android.content.Context
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.view.CoreViewModel

class BudgetsViewModel(context: Context): CoreViewModel(context) {

    fun getBudgets(): List<Budget>? {
        return dataStore.getBudgets()
    }
}