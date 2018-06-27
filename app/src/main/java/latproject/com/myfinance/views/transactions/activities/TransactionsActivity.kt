package latproject.com.myfinance.views.transactions.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.Constants
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivityTransactionsBinding
import latproject.com.myfinance.views.transactiondetails.activities.TransactionDetailsActivity
import latproject.com.myfinance.views.transactions.adapters.TransactionListItemAdapter
import latproject.com.myfinance.views.transactions.viewmodels.TransactionsViewModel

class TransactionsActivity : CoreActivity(), TransactionListItemAdapter.OnTransactionClickedListener {

    lateinit var viewModel: TransactionsViewModel
    lateinit var binding: ActivityTransactionsBinding
    lateinit var bankTransactionsAdapter: TransactionListItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transactions)
        viewModel = TransactionsViewModel(this)

        initRecyclerView()

        loadTransactions()
    }

    private fun initRecyclerView() {
        bankTransactionsAdapter = TransactionListItemAdapter()
        bankTransactionsAdapter.onTransactionClickedListener = this
        binding.transactionsList.layoutManager = LinearLayoutManager(this)
        binding.transactionsList.adapter = bankTransactionsAdapter
    }

    private fun loadTransactions() {
        val bankName = viewModel.getBank()
        if (bankName != null) {
            viewModel.loadTransactions(bankName).forEach {
                if (it != null)
                    bankTransactionsAdapter.addTransaction(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white)
    }

    override fun onTransactionClicked(bankTransaction: RealmBankTransaction) {
        val transactionClickedIntent = Intent(this, TransactionDetailsActivity::class.java)
        transactionClickedIntent.putExtra(Constants.KEY_BANK_TRANSACTION, bankTransaction.id)
        startActivity(transactionClickedIntent)
    }
}
