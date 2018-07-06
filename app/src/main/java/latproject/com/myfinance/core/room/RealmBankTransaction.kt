package latproject.com.myfinance.core.room

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmBankTransaction : RealmObject() {
    var bank: String = ""
    var date: Long = 0L
    var type: String = ""
    var amount: Double = 0.0
    var details: String = ""
    var balanceAfterTransaction: Double = 0.0
    var isCredit: Boolean = false
        get() = type.isNotEmpty() && type == "credit"
    @PrimaryKey
    var id: String = ""

    override fun equals(other: Any?): Boolean {
        val current = other as RealmBankTransaction

        return current.date == this.date && current.amount == this.amount && current.details == this.details
    }

    override fun toString(): String {
        return "BankTransaction(bank='$bank', date=$date, type='$type', amount=$amount, details='$details', balanceAfterTransaction=$balanceAfterTransaction, isCredit=$isCredit, id=$id)"
    }

    override fun hashCode(): Int {
        var result = bank.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + balanceAfterTransaction.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}