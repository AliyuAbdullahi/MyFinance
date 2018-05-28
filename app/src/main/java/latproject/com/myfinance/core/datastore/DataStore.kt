package latproject.com.myfinance.core.datastore

import javax.inject.Inject

class DataStore @Inject constructor(var offlineStore: OfflineStore, var onlineStore: OnlineStore) {


}