package latproject.com.myfinance.views.transactiondetails.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.Constants
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.databinding.ActivityTransactionDetailsBinding
import latproject.com.myfinance.views.transactiondetails.viewmodels.TransactionDetailsViewModel

class TransactionDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityTransactionDetailsBinding
    private var appToggleState: State = State.EXPANDED
    private var viewModel = TransactionDetailsViewModel(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_details)

        obtainTransaction {
            bind(it)
            binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.transparent))
            checkScrollChange()
        }
    }

    private val OPEN = 0
    private fun checkScrollChange() {
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {

                if (verticalOffset == OPEN) {
                    manageCollapseState(State.EXPANDED)

                } else if (Math.abs(verticalOffset) >= appBarLayout!!.getTotalScrollRange()) {
                    manageCollapseState(State.COLLAPSED)
                } else {
                    manageCollapseState(State.IDLE)
                }

            }
        })
    }

    private fun manageCollapseState(state: State) {
        when (state) {
            State.EXPANDED -> {
                if (appToggleState != State.EXPANDED)
                    Toast.makeText(this, "Expanded", Toast.LENGTH_SHORT).show()
                appToggleState = State.EXPANDED
            }
            State.IDLE -> {
                if (appToggleState != State.IDLE)
                    Toast.makeText(this, "IDLE", Toast.LENGTH_SHORT).show()
                appToggleState = State.IDLE
            }
            State.COLLAPSED -> {
                if (appToggleState != State.COLLAPSED)
                    Toast.makeText(this, "COLLAPSED", Toast.LENGTH_SHORT).show()
                appToggleState = State.COLLAPSED
            }
        }
    }

    private fun obtainTransaction(onTransactionObtained: (bankTransaction: RealmBankTransaction) -> Unit) {
        if (intent.hasExtra(Constants.KEY_BANK_TRANSACTION)) {

            val bankTransactionId = intent.getStringExtra(Constants.KEY_BANK_TRANSACTION)

            val bankTransaction = viewModel.transaction(bankTransactionId)
            if (bankTransaction != null)
                onTransactionObtained(bankTransaction)
            else
                Toast.makeText(this, "Error loading transaction", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bind(bankTransaction: RealmBankTransaction) {
        binding.amount.text = resources.getString(R.string.amount, bankTransaction.amount)
        binding.details.text = bankTransaction.details
        binding.balanceAfterTransaction.text = resources.getString(R.string.amount, bankTransaction.balanceAfterTransaction)
        binding.collapsingToolbar.title = if (bankTransaction.isCredit) getString(R.string.credit) else getString(R.string.debit)
    }

    enum class State {
        COLLAPSED,
        EXPANDED,
        IDLE
    }
}
