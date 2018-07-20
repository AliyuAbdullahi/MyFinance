package latproject.com.myfinance.views.transactions.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.Constants
import latproject.com.myfinance.core.globals.makeToast
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
        setSupportActionBar(binding.toolbar)
        bindStatusBar()
        viewModel = TransactionsViewModel(this)

        initRecyclerView()

        setToolBarTitle()

        loadTransactions()
    }

    private fun setToolBarTitle() {
        binding.toolbarTitle.text = "${viewModel.getBank()} (Transactions)"
    }

    private fun bindStatusBar() {
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home ->
                    onBackPressed()
        }
        return super.onOptionsItemSelected(item)
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
            val list = viewModel.getAllTransactionsForBank(bankName)?.reversed()
            list?.forEach {
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
