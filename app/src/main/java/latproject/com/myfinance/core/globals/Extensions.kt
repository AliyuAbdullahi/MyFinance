package latproject.com.myfinance.core.globals

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun<reified T:Context> Activity.navigateTo() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}