package latproject.com.myfinance.views.budgets.activities

import android.os.Bundle
import latproject.com.myfinance.R
import latproject.com.myfinance.core.view.CoreActivity

class BudgetsListActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budgets)
       }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white)
    }
}
