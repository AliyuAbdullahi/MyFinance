package latproject.com.myfinance.core.datastore.realm

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import timber.log.Timber

class RealmManager(private val realm: Realm) {

    init {
        Timber.d("DI - RealmManager with realm: $realm")
    }

    fun saveOrUpdate(realmObject: RealmObject) {
        // Purposefully choosing code reuse over performance here.
        saveOrUpdate(listOf(realmObject))
    }

    fun saveOrUpdate(realmObjects: List<RealmObject>) {
        realm.executeTransaction { realm -> realmObjects.forEach { realm.insertOrUpdate(it) } }
    }

    fun delete(realmModel: RealmObject) {
        realm.executeTransaction { realmModel.deleteFromRealm() }
    }

    fun save(realmModel: RealmObject) {
        realm.executeTransaction{realm -> realm.insert(realmModel)}
    }

    fun save(realmObjects: List<RealmObject>) {
        realm.executeTransaction { realm -> realmObjects.forEach { realm.insert(it) } }
    }

    fun <T : RealmObject?> findById(
            clazz: Class<T>,
            idFieldName: String,
            id: Int): T? {
        return realm.where(clazz).equalTo(idFieldName, id).findFirst()
    }

    fun <T : RealmObject?> findById(
            clazz: Class<T>,
            idFieldName: String,
            id: String): T? {
        return realm.where(clazz).equalTo(idFieldName, id).findFirst()
    }

    fun <T : RealmObject> findAllByClass(clazz: Class<T>): List<T> {
        return realm.where(clazz).findAll()
    }

    fun <T : RealmObject> findObjectsAllByClass(clazz: Class<T>): List<T> {
        return realm.where(clazz).findAll()
    }


    fun <T: RealmObject> findOne(clazz: Class<T>): T? {
        val all = realm.where(clazz)
        return all.findFirst()
    }

    fun <T : RealmObject> findAllByClassDetached(
            clazz: Class<T>,
            realmQuery: ((RealmQuery<T>) -> RealmQuery<T>)? = null
    ): List<T> {

        var ourQuery = realm.where(clazz)

        if (realmQuery != null) {
            ourQuery = realmQuery(ourQuery)
        }

        return ourQuery
                .findAll()
                .map { realm.copyFromRealm(it) }
    }

    fun <T : RealmObject> clearAll(clazz: Class<T>) {
        val all = realm.where(clazz).findAll()
        realm.executeTransaction { all.deleteAllFromRealm() }
    }

    fun deleteAll() {
        executeWithLocalRealmTransaction(Realm::deleteAll)
    }

    /**
     * Realm instances need to be closed after being used. This utility function does that
     * automatically for the closure passed in.
     */
    fun <T> executeWithLocalRealm(execute: (Realm) -> T): T {
        val baseRealm = realm

        var realm: Realm? = null

        val configuration = requireNotNull(baseRealm.configuration, { "realmConfiguration" })

        try {
            realm = Realm.getInstance(configuration)
            return execute(realm)
        }
        catch (e: Throwable) {
            Timber.e(e, "[REALM] Error while executing Realm operation")
            throw e
        }
        finally {
            realm?.close()
        }
    }

    /**
     * Realm instances need to be closed after being used. This utility function does that
     * automatically for the closure passed in.
     */
    fun executeWithLocalRealmTransaction(transaction: (Realm) -> Unit) {
        executeWithLocalRealm { it.executeTransaction { transaction(it) } }
    }

    fun getRealm(): Realm {
        return realm
    }

    fun <T : RealmObject?> where(clazz: Class<T>): RealmQuery<T> = realm.where(clazz)
}