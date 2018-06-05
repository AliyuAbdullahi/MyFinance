package latproject.com.myfinance.core.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "BankTransactions")
class BankTransaction: Serializable {
    var bank: String = ""
    var date: Long  = 0L
    var type: String = ""
    var amount: Double = 0.0
    var details: String = ""
    var balanceAfterTransaction: Double = 0.0
    var isCredit: Boolean = false
    get() = type.isNotEmpty() && type == "credit"
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun equals(other: Any?): Boolean {
        val transaction = other as BankTransaction

        return this.date == transaction.date && this.type == transaction.type && this.bank == transaction.bank
    }

    override fun hashCode(): Int {
        var result = bank.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + id
        return result
    }

    override fun toString(): String {
        return "BankTransaction(bank='$bank', date=$date, type='$type', amount=$amount, details='$details', balanceAfterTransaction=$balanceAfterTransaction, isCredit=$isCredit, id=$id)"
    }
}