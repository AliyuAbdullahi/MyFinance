package latproject.com.myfinance.core.globals

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import latproject.com.myfinance.R

inline fun<reified T:Context> Activity.navigateTo() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

inline fun<reified T: Context> Activity.navigateTo(bundle: Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
}

fun EditText.clear() {
    this.setText(this.context.getString(R.string.empty))
}

fun Activity.makeToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}