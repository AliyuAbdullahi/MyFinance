package latproject.com.myfinance.core.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "User")
class User {
    @PrimaryKey
    var id: String = ""
    var email: String = ""
    var name: String = ""
    var bank: String = ""
    var bankTextColor: String = ""
    var bankBackGroundColor: String = ""

    override fun equals(other: Any?): Boolean {
        val targetUser = other as User

        if (this.email.isEmpty() && targetUser.email.isNotEmpty() || this.name.isEmpty() && other.name.isNotEmpty()) {
            return false
        }

        return targetUser.email == this.email || targetUser.name == this.name
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + bank.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(id='$id', email='$email', name='$name', bank='$bank', bankTextColor='$bankTextColor', bankBackGroundColor='$bankBackGroundColor')"
    }


}