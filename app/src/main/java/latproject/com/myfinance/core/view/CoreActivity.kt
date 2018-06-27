package latproject.com.myfinance.core.view

import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

open class CoreActivity: RxAppCompatActivity() {

    fun setStatusBarColor(color: Int) {
        runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this@CoreActivity, color)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.statusBarColor = getColor(color)
                }
            }
        }
    }
}