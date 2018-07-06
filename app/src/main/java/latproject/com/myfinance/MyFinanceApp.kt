package latproject.com.myfinance
import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber

class MyFinanceApp: Application() {
    override fun onCreate() {
        super.onCreate()

        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)

        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded() // TODO DEV PURPOSES ONLY
                .build()

        Timber.d("DI - RealmConfig: $realmConfig")
        Realm.setDefaultConfiguration(realmConfig)
    }
}