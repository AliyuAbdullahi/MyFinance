package latproject.com.myfinance.views.money_spend_overview.viewmodels

import android.content.Context
import latproject.com.myfinance.core.model.PieArcObjectFroTransaction
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.view.CoreViewModel

class MoneySpentOverviewViewModel(context: Context): CoreViewModel(context) {

    private fun getMyTransactions(): List<RealmBankTransaction>? {
        val bankName = getBankName()
        if(bankName != null) {
            return getTransactionsForBank(bankName)
        }

        return listOf()
    }


    fun getTransactionsFrom(numberOfDays: Int = 30): List<RealmBankTransaction>? {
        val timeAtDay = 86400000L * numberOfDays

        val daysAway = System.currentTimeMillis() - timeAtDay

        return getMyTransactions()?.filter { it.date >= daysAway}
    }

    fun getPieChatData(realmTransactions: List<RealmBankTransaction>):MutableList<PieArcObjectFroTransaction> {
        var pieArchObjectList = mutableListOf<PieArcObjectFroTransaction>()
        val totalAmount = Math.abs(realmTransactions.first().amount - realmTransactions.last().amount)

        realmTransactions.groupBy { it.source() }.forEach {

            var amountSpent = 0.0

            it.value.forEach {
                amountSpent += it.amount
            }

            val valueInPercent = (amountSpent / totalAmount) * 100
            pieArchObjectList.add(PieArcObjectFroTransaction(it.key, amountSpent, valueInPercent.toFloat()))
        }

        return pieArchObjectList
    }
}