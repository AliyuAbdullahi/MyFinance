package latproject.com.myfinance.core.model

import io.realm.RealmObject

open class RealmBank: RealmObject() {
    var backgroundColor: String? = null
    var logo: String? = null
    var name: String? = null
    var textColor: String? = null

    override fun toString(): String {
        return "Bank{" +
                "mBackgroundColor='" + backgroundColor + '\''.toString() +
                ", mLogo='" + logo + '\''.toString() +
                ", mName='" + name + '\''.toString() +
                ", mTextColor='" + textColor + '\''.toString() +
                '}'.toString()
    }
}