package latproject.com.myfinance.core.room

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Budget: RealmObject() {
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
    var fulfilled: Boolean = false

    override fun toString(): String {
        return "Budget(id=$id, amount=$amount, durationInDays=$durationInDays, fulfilled=$fulfilled)"
    }
}