package latproject.com.myfinance.views.selectbank

import android.widget.ImageView
import latproject.com.myfinance.R
import latproject.com.myfinance.core.model.Bank

class BankParser {
    companion object {
        fun parse(bank: Bank, imageView: ImageView) {
          when(bank.logo) {
              "stanbic" -> {
                  imageView.setImageResource(R.drawable.stanbic_ibtc_logo)
              }
          }
        }
    }
}