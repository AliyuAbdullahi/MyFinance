package latproject.com.myfinance.views.transactions.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.Toast
import latproject.com.myfinance.R
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivityTransactionsBinding
import latproject.com.myfinance.views.transactions.viewmodels.TransactionsViewModel

class TransactionsActivity : CoreActivity() {
    lateinit var viewModel: TransactionsViewModel
    lateinit var binding: ActivityTransactionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transactions)
        viewModel = TransactionsViewModel(this)


    }
}
