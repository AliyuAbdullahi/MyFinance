package latproject.com.myfinance.views.budgets.viewmodels

import android.content.Context
import android.databinding.Bindable
import latproject.com.myfinance.BR
import latproject.com.myfinance.core.model.SmsMessage
import latproject.com.myfinance.core.model.modelparser.TransactionParser
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.services.sms.SmsReader
import latproject.com.myfinance.core.view.CoreViewModel

class CreateBudgetViewModel(context: Context) : CoreViewModel(context) {

    fun getBaseBalance(): Double {
        val currentBank = dataStore.getBank()
        val base = dataStore.getAllTransactions()?.map { it.balanceAfterTransaction }?.last()

//        val base = dataStore.getTransactionsForBank(currentBank!!)?.map { it.balanceAfterTransaction }?.last()

        return base ?: 0.00
    }

    fun getAllBalance() : Int?  {
        return dataStore.getAllTransactions()?.size
    }

    fun getLastCredit(): Double {
        val currentBank = dataStore.getBank()

        val lastCredit = dataStore.getTransactionsForBank(currentBank!!)?.filter { it.isCredit }?.map { it.amount }?.last()

        return lastCredit ?: 0.00
    }

    @Bindable
    var base: Double = 0.00
    set(value) {
        field = value
        notifyPropertyChanged(BR.base)
        notifyPropertyChanged(BR.currentBase)
    }

    @Bindable
    var currentBase : String = ""
    get() = "$base"
    set(value) {
        field = value
        notifyPropertyChanged(BR.currentBase)
    }

    @Bindable
    var currentBank: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentBank)
        }
        get() = getBank() ?: ""

    fun getBank(): String? {
        return dataStore.getBank()
    }

    fun createBudget(budget: Budget, onBudgetCreated: (budget: Budget?) -> Unit) {
        dataStore.createBudget(budget)

        val allBudgets = dataStore.getBudgets()

        onBudgetCreated(allBudgets?.first { it.id == budget.id })
    }
}