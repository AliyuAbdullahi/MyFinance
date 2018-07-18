package latproject.com.myfinance.core.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class OnboardingStatus :RealmObject() {
    @PrimaryKey
    var id:String = ""
    var status:Boolean = false

    override fun toString(): String {
        return "OnboardingStatus(id='$id', status=$status)"
    }
}