package latproject.com.myfinance.views.selectbank.utils

import android.widget.ImageView
import latproject.com.myfinance.R
import latproject.com.myfinance.core.model.RealmBank

class BankParser {
    companion object {
        fun parse(bank: RealmBank, imageView: ImageView) {
            when (bank.logo) {
                "stanbic" -> {
                    imageView.setImageResource(R.drawable.stanbic_ibtc_logo)
                }
                "gtb" -> {
                    imageView.setImageResource(R.drawable.gtb_logo)
                }
                "union" -> {
                    imageView.setImageResource(R.drawable.union_bank_logo)
                }

                "uba" -> {
                    imageView.setImageResource(R.drawable.uba_logo)
                }
            }
        }
    }
}