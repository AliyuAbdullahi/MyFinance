package latproject.com.myfinance.views.budgets.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import latproject.com.myfinance.R
import latproject.com.myfinance.databinding.ActivityCreateBudgetBinding
import latproject.com.myfinance.views.budgets.viewmodels.CreateBudgetViewModel

class CreateBudgetActivity : AppCompatActivity() {
    private lateinit var viewModel: CreateBudgetViewModel
    lateinit var binding: ActivityCreateBudgetBinding
    var budgetAmount: Double = 0.0
    var currentBudgetDuration = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = CreateBudgetViewModel(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_budget)
    }

    inner class AmountWatcher : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            checkIfAmountIsMoreThanAvailable()
        }
    }

    private fun checkIfAmountIsMoreThanAvailable() {
        if (budgetAmount >= viewModel.getBaseBalance()) {
        //show budget is more than expectations
        }
    }

    private fun suggestAmount() {
        //suggest 50% of income 50/30/20
    }
}
