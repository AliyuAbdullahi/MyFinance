package latproject.com.myfinance.views.homescreen.viewmodels

import android.content.Context
import latproject.com.myfinance.core.datastore.DataStore
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.view.CoreViewModel
import javax.inject.Inject

class HomeActivityViewModel (val context: Context): CoreViewModel(context) {

    fun getBudgets(): List<Budget>? {
       return dataStore.getBudgets()
    }

    fun getBudgetsBy(bankName: String): List<Budget>? {
        return dataStore.getBudgetsForABank(bankName)
    }
}