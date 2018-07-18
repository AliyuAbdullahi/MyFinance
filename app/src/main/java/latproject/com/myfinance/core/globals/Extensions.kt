package latproject.com.myfinance.core.globals

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import latproject.com.myfinance.R

inline fun <reified T : Context> Activity.navigateTo() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

inline fun <reified T : Context> Activity.navigateTo(bundle: Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
}

fun EditText.clear() {
    this.setText(this.context.getString(R.string.empty))
}

fun Context.toastLong(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastShort(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.makeToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

private const val digits = ".0123456789"
private val char_digits = arrayOf('.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
fun String.isADigit(): Boolean {
    return digits.contains(this)
}

fun Char.isACharDigit(): Boolean {
    return char_digits.contains(this)
}

