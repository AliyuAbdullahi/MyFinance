package latproject.com.myfinance.views.homescreen.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import latproject.com.myfinance.R
import latproject.com.myfinance.core.view.CoreActivity

class HomeActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
    }
}
