package latproject.com.roomtutorial

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "BankTransactions")
class BankTransaction {
    var firstName: String = ""
    var lastName: String = ""
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun toString(): String {
        return "Person(firstName='$firstName', lastName='$lastName', id='$id')"
    }
}