package latproject.com.myfinance.views.homescreen.activities

import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.makeToast
import latproject.com.myfinance.core.globals.navigateTo
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.services.PermissionManager
import latproject.com.myfinance.core.services.sms.SmsListener
import latproject.com.myfinance.core.services.sms.SmsReceiver
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivityHomeBinding
import latproject.com.myfinance.views.budgets.activities.CreateBudgetActivity
import latproject.com.myfinance.views.homescreen.viewmodels.HomeActivityViewModel

class HomeActivity : CoreActivity(), SmsListener {
    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeActivityViewModel
    var permissionManager: PermissionManager? = null

    override fun onMessageReceived(message: String) {
        //TODO process message notification here...
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.handler = HomeActivityHandler()
        viewModel = HomeActivityViewModel(this)
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white75)
    }

    private fun fetchBudget() {
        val budgets = viewModel.getBudgets()
        if (budgets == null || budgets.isEmpty()) {
            setUpProgress(0.0, 0.0)
        } else {
            val bankName = viewModel.getBankName()
            val activeBudget = budgets.last { it.active }
            val allTrxs = viewModel
                    .getTransactionsForBank(bankName!!)?.filter { it.date > activeBudget.dateSet && !it.isCredit }?.map { it.amount }?.sum()

            setUpProgress(allTrxs ?: 0.0, activeBudget.amount)
        }
    }

    override fun onResume() {
        super.onResume()
        cacheTransactions()
    }

    private fun cacheTransactions() {
        if (permissionManager == null) {
            permissionManager = PermissionManager(this)
            if (permissionManager!!.checkPermissionForSmsRead())
                loadTransactions()
            else
                permissionManager!!.requestPermissionToReadSms()
            permissionManager!!.checkPermissionForSmsRead()
        } else {
            if (permissionManager!!.checkPermissionForSmsRead())
                loadTransactions()
            else
                permissionManager!!.checkPermissionForSmsRead()
        }
    }

    private fun setUpProgress(amountSpent: Double, amount: Double) {
        var offset = 0.0
        var valueInPercent = amountSpent / amount * 100

        if (valueInPercent > 100) {
            offset = valueInPercent - 100
            valueInPercent = 100.0
        }

        //cacheOffsetOnBudgetSoYouCanWorkWithItInNextBudget

        binding.moneySpent.text = "$amountSpent"

        binding.baseAmount.text = "$amount"

        binding.budgetProgressBar.progressBarWidth = resources.getDimension(R.dimen.size_16dp)
        binding.budgetProgressBar.backgroundColor = ContextCompat.getColor(this, R.color.budget_default)
        binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_fine)

        Handler().postDelayed({
            binding.budgetPercentUsed.visibility = View.VISIBLE
            if (amountSpent == 0.0) {
                binding.budgetPercentUsed.text = "-"
            } else {
                binding.budgetPercentUsed.text = "${String.format("%.0f", valueInPercent)}%"
            }
        }, 2500)

        if (valueInPercent <= 0.0) {
            binding.budgetProgressBar.setProgressWithAnimation(valueInPercent.toFloat() + 2, 2500)
        } else {
            if (valueInPercent >= 90) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_bad)
            }
            if (valueInPercent >= 70) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_going_bad)
            }

            if (valueInPercent >= 50) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_weird)
            }

            if (valueInPercent >= 30) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_going_off)
            }
            if (valueInPercent < 30) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_fine)
            }
            binding.budgetProgressBar.setProgressWithAnimation(valueInPercent.toFloat(), 2500)
        }
    }

    private fun loadTransactions() {
        val bankName = viewModel.getBankName()
        val all = ArrayList<RealmBankTransaction>()

        if (permissionManager!!.checkPermissionForSmsRead()) {
            for (loadTransaction in viewModel.loadTransactions(bankName!!)) {
                if (loadTransaction != null) {
                    all.add(loadTransaction)
                }
            }

            all.forEach { viewModel.saveRealmTransaction(it) }

            fetchBudget()
        } else {
            permissionManager!!.checkPermissionForSmsRead()
        }
    }

    private fun initSms() {
        SmsReceiver.bindListener(this)
        val bankName = viewModel.getBankName()
        if (bankName != null) {
            makeToast("Saved ${bankName}")
            viewModel.loadTransactions(bankName).forEachIndexed { index, value ->
                if (index < 4)
                    makeToast("Saved ${value}")

            }
        } else {
            makeToast("Bank name is null")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadTransactions()
        }
    }

    fun navigateToBudgetCreationPage() {
        navigateTo<CreateBudgetActivity>()
    }

    inner class HomeActivityHandler {
        fun onCreateBudgetClicked(view: View) {
            navigateToBudgetCreationPage()
        }

        fun onTransactionsClicked(view: View) {
            makeToast("Transactions clicked")
        }

        fun onBudgetsClicked(view: View) {
            makeToast("Budgets list clicked")

        }
    }
}
