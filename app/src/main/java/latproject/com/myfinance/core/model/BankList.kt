package latproject.com.myfinance.core.model

import com.google.gson.annotations.SerializedName

class BankList {

    @SerializedName("banks")
    var banks: List<Bank>? = null

}
