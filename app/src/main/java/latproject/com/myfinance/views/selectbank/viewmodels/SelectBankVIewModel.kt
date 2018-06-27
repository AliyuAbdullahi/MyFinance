package latproject.com.myfinance.views.selectbank.viewmodels

import android.content.Context
import android.databinding.Bindable
import com.google.gson.Gson
import latproject.com.myfinance.BR
import latproject.com.myfinance.core.model.Bank
import latproject.com.myfinance.core.model.BankList
import latproject.com.myfinance.core.utils.JsonProcessorFromAsset
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.core.view.CoreViewModel

class SelectBankVIewModel(context: Context) : CoreViewModel(context) {
    fun getAllBanks(activity: CoreActivity, onBanksObtained: (banks: List<Bank>) -> Unit) {

        val jasonProcessorFromAsset = JsonProcessorFromAsset()
        val json = jasonProcessorFromAsset.loadJSONFromAsset(activity, "banks.json")

        val gson = Gson()
        val banks = gson.fromJson<BankList>(json, BankList::class.java)
        if (banks != null) {
            onBanksObtained(banks.banks!!)
        }
    }

    @Bindable
    var dialogShowing: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.dialogShowing)
        }
}