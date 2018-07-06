package latproject.com.myfinance.core.room
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User: RealmObject() {
    @PrimaryKey
    var id: String = ""
    var email: String? = null
    var name: String? = null
    var bank: String? = null
    var bankTextColor: String? = null
    var bankBackGroundColor: String? = null

    override fun equals(other: Any?): Boolean {
        val targetUser = other as User

        if (this.email.isNullOrEmpty() && targetUser.email.isNullOrEmpty().not() || this.name.isNullOrEmpty() && other.name.isNullOrEmpty().not()) {
            return false
        }

        return targetUser.email == this.email || targetUser.name == this.name
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "User(id='$id', email='$email', name='$name', bank='$bank', bankTextColor='$bankTextColor', bankBackGroundColor='$bankBackGroundColor')"
    }
}