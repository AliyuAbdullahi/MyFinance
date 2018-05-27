package latproject.com.myfinance.core.dagger.modules

import dagger.Provides
import latproject.com.myfinance.core.datastore.OfflineStore
import latproject.com.myfinance.core.datastore.OnlineStore

class DataBaseModule {

    @Provides
    fun provideOfflineStore(): OfflineStore {
        return OfflineStore()
    }

    @Provides
    fun provideOnlineStore(): OnlineStore {
        return OnlineStore()
    }
}