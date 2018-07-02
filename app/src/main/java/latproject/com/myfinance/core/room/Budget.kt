package latproject.com.myfinance.core.room

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Budget : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var amount: Double = 0.0
    var bank: String = ""
    var amountSpent: Double = 0.0
    var baseAmount: Double = 0.0
    var dateSet: Long = 0L
    var dateFinished: Long = 0L
    var durationInDays: Int = 0
    var active: Boolean = false
    var status: String = ""
    var fulfilled: Boolean = false

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun getCurrentStatus():String {
        return if (active) "Active" else if (dateFinished > System.currentTimeMillis()) "Not Active" else "Not active"
    }

    override fun equals(other: Any?): Boolean {
        val current = other as Budget

        return current.id == this.id
    }

    override fun toString(): String {
        return "Budget(id='$id', amount=$amount, bank='$bank', amountSpent=$amountSpent, baseAmount=$baseAmount, dateSet=$dateSet, dateFinished=$dateFinished, durationInDays=$durationInDays, active=$active, status='$status', fulfilled=$fulfilled)"
    }
}