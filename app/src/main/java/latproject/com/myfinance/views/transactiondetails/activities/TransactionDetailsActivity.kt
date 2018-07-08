package latproject.com.myfinance.views.transactiondetails.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.widget.Toast
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.Constants
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.utils.FormattingUtility
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivityTransactionDetailsBinding
import latproject.com.myfinance.views.transactiondetails.viewmodels.TransactionDetailsViewModel
import java.util.*

class TransactionDetailsActivity : CoreActivity() {
    lateinit var binding: ActivityTransactionDetailsBinding
    private var appToggleState: State = State.EXPANDED
    private var viewModel = TransactionDetailsViewModel(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_details)
        setSupportActionBar(binding.toolbar)
        bindStatusBar()
        obtainTransaction {
            bind(it)
            binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.transparent))
            checkScrollChange()
        }
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white)
    }

    private fun bindStatusBar() {
        val actionbar = supportActionBar
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setDisplayShowTitleEnabled(false)
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
                appToggleState = State.EXPANDED
            }
            State.IDLE -> {
                if (appToggleState != State.IDLE)
                appToggleState = State.IDLE
            }
            State.COLLAPSED -> {
                if (appToggleState != State.COLLAPSED)
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
        val imageResources = arrayListOf<Int>(R.drawable.transaction_1,
                R.drawable.transaction_2,R.drawable.transaction_3,
                R.drawable.transaction_4,R.drawable.transaction_5,
                R.drawable.transaction_6)

        val rand =  Random()
        val randInt = rand.nextInt( 6 )
        binding.amount.text = resources.getString(R.string.amount, bankTransaction.amount)
        binding.details.text = bankTransaction.details.replace("   ", " ").replace("  ", " ")
        binding.balanceAfterTransaction.text = resources.getString(R.string.amount, bankTransaction.balanceAfterTransaction)
        binding.collapsingToolbar.title = if (bankTransaction.isCredit) getString(R.string.credit) else getString(R.string.debit)
        binding.date.text = FormattingUtility.getDateTime(bankTransaction.date)
        binding.jobDetailsToolbarImage.setImageResource(imageResources[randInt])
    }

    enum class State {
        COLLAPSED,
        EXPANDED,
        IDLE
    }
}
