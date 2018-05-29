package latproject.com.myfinance.views.selectbank.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import latproject.com.myfinance.R

import kotlinx.android.synthetic.main.activity_select_bank.*
import latproject.com.myfinance.core.view.CoreActivity

class SelectBank : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_bank)
        setSupportActionBar(toolbar)
    }
}
