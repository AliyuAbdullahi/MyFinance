package latproject.com.myfinance.core.model

import com.google.gson.annotations.SerializedName

class Bank {

    @SerializedName("background_color")
    var backgroundColor: String? = null
    @SerializedName("logo")
    var logo: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("text_color")
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
