package latproject.com.myfinance.core.model

import latproject.com.myfinance.core.room.RealmBankTransaction
import java.io.Serializable

class Transactions: Serializable {
    var transactions: List<RealmBankTransaction>? = null
}