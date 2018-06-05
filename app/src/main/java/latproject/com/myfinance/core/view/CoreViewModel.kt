package latproject.com.myfinance.core.view

import android.content.Context
import android.databinding.BaseObservable
import latproject.com.myfinance.core.datastore.DataStore

open class CoreViewModel(context: Context): BaseObservable() {
    var dataStore = DataStore(context)
}