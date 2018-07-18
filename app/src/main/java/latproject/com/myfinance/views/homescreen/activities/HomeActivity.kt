package latproject.com.myfinance.views.homescreen.activities

import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.PopupMenu
import android.telephony.SmsMessage
import android.view.MenuItem
import android.view.View
import com.roger.catloadinglibrary.CatLoadingView
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.Constants
import latproject.com.myfinance.core.globals.makeToast
import latproject.com.myfinance.core.globals.navigateTo
import latproject.com.myfinance.core.model.Transactions
import latproject.com.myfinance.core.model.modelparser.TransactionParser
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.services.PermissionManager
import latproject.com.myfinance.core.services.sms.SmsListener
import latproject.com.myfinance.core.services.sms.SmsReceiver
import latproject.com.myfinance.core.services.sms.SmsService
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivityHomeBinding
import latproject.com.myfinance.views.budgets.activities.BudgetsListActivity
import latproject.com.myfinance.views.budgets.activities.CreateBudgetActivity
import latproject.com.myfinance.views.dialogfragments.NotificationAlertDialog
import latproject.com.myfinance.views.homescreen.viewmodels.HomeActivityViewModel
import latproject.com.myfinance.views.money_spend_overview.activities.MoneySpendingStatActivity
import latproject.com.myfinance.views.selectbank.activities.SelectBankActivity
import latproject.com.myfinance.views.settings.activities.SettingsActivity
import latproject.com.myfinance.views.transactions.activities.TransactionsActivity
import timber.log.Timber
import java.util.*

class HomeActivity : CoreActivity(), SmsListener,
        NotificationAlertDialog.OnOkayClickedListener,
        PopupMenu.OnMenuItemClickListener {

    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeActivityViewModel
    var permissionManager: PermissionManager? = null
    val catLoadingView = CatLoadingView()

    override fun onMessageReceived(message: SmsMessage) {
        try {
            val bankName = requireNotNull(viewModel.getBankName())
            if (message.displayOriginatingAddress.toLowerCase().contains(bankName.toLowerCase().substring(0, bankName.length / 2))) {

                val sms = latproject.com.myfinance.core.model.SmsMessage()
                sms.body = message.displayMessageBody
                sms.from = message.displayOriginatingAddress
                sms.date = message.timestampMillis

                val bankTransaction = TransactionParser.parseToTransaction(bankName, sms)

                if (bankTransaction != null) {
                    viewModel.saveRealmTransaction(bankTransaction)
                }
            }
        } catch (ex: IllegalStateException) {
            Timber.e("ERROR %s", ex)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.handler = HomeActivityHandler()
        viewModel = HomeActivityViewModel(this)
        binding.viewModel = viewModel

        if (viewModel.currentBank() == null || viewModel.currentBank()!!.name == null) {
            getCurrentlySelectedBank()
        } else {

            showLoading()
            initSms()
            initSmsServiceIntent()
            Handler().postDelayed({
                runOnUiThread {
                    hideLoading()
                    loadTransactions()
                }
            }, 2000)
        }
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white)
        registerReceiver()
    }

    private fun getCurrentlySelectedBank() {
        val bank = viewModel.currentBank()

        if (bank?.name == null) {
            val intent = Intent(this, SelectBankActivity::class.java)
            intent.putExtra(Constants.FROM_ANOTHER_ACTIVITY, true)
            startActivity(intent)
            finish()
        }

        binding.currentBank.text = bank!!.name
        binding.currentBank.setTextColor(Color.parseColor(bank.textColor))
    }

    private fun updateBudgetsUnderTheHood() {
        val allBudgets = viewModel.getBudgets()
        if (allBudgets != null && allBudgets.isNotEmpty()) {
            allBudgets.forEach {
                if (System.currentTimeMillis() >= it.dateFinished) {
                    val budget = it
                    viewModel.dataStore.getRealm().executeTransaction({
                        budget.active = false
                    })
                }
            }
        }
    }

    private fun fetchBudget() {
        val budgets = viewModel.getBudgets()
        if (budgets == null || budgets.isEmpty()) {
            setUpProgress(0.0, 0.0)
            NotificationAlertDialog.show(supportFragmentManager, getString(R.string.you_have_not_created_budget_yet), getString(R.string.no_budget_available))
        } else {
            val bankName = viewModel.getBankName()
            val activeBudget = budgets.find { it.active }
            if (activeBudget != null) {
                val allTrxs = viewModel
                        .getTransactionsForBank(bankName!!)?.filter { it.date > activeBudget.dateSet && it.date <= activeBudget.dateFinished && !it.isCredit }?.map { it.amount }?.sum()

                setUpProgress(allTrxs ?: 0.0, activeBudget.amount)
            } else {
                NotificationAlertDialog.show(supportFragmentManager, getString(R.string.you_dont_have_active_budget), getString(R.string.no_active_budget))
                setUpProgress(0.0, 0.0)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.money_spent -> {
                navigateTo<MoneySpendingStatActivity>();return true
            }
            R.id.budget_activities -> {
                makeToast("Will be available soon.");return true
            }
        }

        return false
    }

    override fun onResume() {
        super.onResume()
        getCurrentlySelectedBank()

        if (viewModel.getBankName() != null) {

            viewModel.loadTransactionsUnderTheHood().forEachIndexed { index, smsMessage ->
                if (index < 6) {
//                val currentTransaction = TransactionParser.parseToTransaction("union", smsMessage)
//                makeToast("${currentTransaction}")
                }
            }

            updateBudgetsUnderTheHood()

            Handler().postDelayed({
                hideLoading()
                fetchBudget()
            }, 2000)
        }
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE
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

    private fun showAccountLimitDialog() {
        vibrate()
        AlertDialog.Builder(this).setTitle(getString(R.string.budget_exhausted))
                .setMessage(getString(R.string.create_another_budget))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.yes), { a, b ->
                    navigateToBudgetCreationPage()
                    a.dismiss()
                }).setNegativeButton(getString(R.string.no), null)
                .create().show()
    }

    private fun setUpProgress(amountSpent: Double, amount: Double) {
        var offset = 0.0
        var valueInPercent = amountSpent / amount * 100

        if (valueInPercent >= 100) {
            offset = valueInPercent - 100
            valueInPercent = 100.0

            Handler().postDelayed({
                showAccountLimitDialog()
            }, 5000)
        }

        //cacheOffsetOnBudgetSoYouCanWorkWithItInNextBudget

        binding.moneySpent.text = String.format("%.0f", amountSpent)

        binding.baseAmount.text = String.format("%.0f", amount)

        binding.budgetProgressBar.progressBarWidth = resources.getDimension(R.dimen.size_16dp)
        binding.budgetProgressBar.backgroundColor = ContextCompat.getColor(this, R.color.budget_default)
        Handler().postDelayed({
            binding.budgetPercentUsed.visibility = View.VISIBLE
            if (amountSpent == 0.0) {
                binding.budgetPercentUsed.text = "-"
            } else {
                binding.budgetPercentUsed.text = "${String.format("%.0f", valueInPercent)}%"
            }
        }, 2500)

        if (valueInPercent <= 0.0) {
            binding.budgetProgressBar.setProgressWithAnimation(valueInPercent.toFloat() + 1, 2200)
        } else {
            if (valueInPercent >= 90) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_bad)
            } else if (valueInPercent >= 70) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_going_bad)
            } else if (valueInPercent >= 50) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_weird)
            } else if (valueInPercent >= 30) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_going_off)
            } else if (valueInPercent < 30) {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_fine)
            } else {
                binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_fine)
            }

            binding.budgetProgressBar.setProgressWithAnimation(valueInPercent.toFloat(), 2500)
        }
    }

    var all: ArrayList<RealmBankTransaction> = arrayListOf<RealmBankTransaction>()
    private fun loadTransactions() {
        val bankName = viewModel.getBankName()
        all = ArrayList<RealmBankTransaction>()

        if (viewModel.getBankName() == null) {
            navigateTo<SelectBankActivity>()
            finish()
        }

        if (permissionManager!!.checkPermissionForSmsRead()) {

            if (viewModel.getBankName() == null) {
                navigateTo<SelectBankActivity>()
                finish()
            } else {
                bindBank()
                for (loadTransaction in viewModel.loadTransactions(bankName!!)) {
                    if (loadTransaction != null) {
                        all.add(loadTransaction)
                    }
                }

                all.forEach { viewModel.saveRealmTransaction(it) }
            }
        } else {
            permissionManager!!.checkPermissionForSmsRead()
        }
    }

    private fun vibrate() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v.vibrate(500)
        }
    }

    private fun bindBank() {
        var bank = viewModel.currentBank()
        if (bank != null) {
            binding.currentBank.visibility = View.VISIBLE
            binding.currentBank.text = bank.name
            binding.currentBank.setTextColor(Color.parseColor(bank.textColor))
        }
    }

    private fun initSms() {
        SmsReceiver.bindListener(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadTransactions()
        }
    }

    fun navigateToBudgetCreationPage() {
        navigateTo<CreateBudgetActivity>()
    }

    fun navigateToTransactions() {
        navigateTo<TransactionsActivity>()
    }

    fun navigateToBudgets() {
        navigateTo<BudgetsListActivity>()
    }

    private fun displayPopUp(view: View) {
        val popup = PopupMenu(this, view)
        val inflater = popup.menuInflater
        popup.setOnMenuItemClickListener(this)
        inflater.inflate(R.menu.transactions_pop_up, popup.getMenu())
        popup.show()
    }

    inner class HomeActivityHandler {
        fun onCreateBudgetClicked(view: View) {
            navigateToBudgetCreationPage()
        }

        fun onTransactionsClicked(view: View) {
            navigateToTransactions()
        }

        fun onBudgetsClicked(view: View) {
            navigateToBudgets()
        }

        fun openSettingsClicked(view: View) {
            navigateTo<SettingsActivity>()
        }

        fun showPopUp(view: View) {
            displayPopUp(view)
        }
    }

    private fun showProgress() {
        binding.whiteBackground.visibility = View.GONE
        catLoadingView.show(supportFragmentManager, "")
    }

    private fun hideProgress() {
        binding.whiteBackground.visibility = View.GONE
        try {
            catLoadingView.dismissAllowingStateLoss()
        } catch (error: IllegalStateException) {
            Timber.e(error)
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    override fun onOkayClicked() {
        navigateToBudgetCreationPage()
    }

    private fun registerReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadCastReceiver, IntentFilter(Constants.SMS_TRANSACTION_LIST_INTENT))
    }

    fun unRegisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterReceiver()
    }

    internal var serviceBounded: Boolean = false

    private lateinit var smsService: SmsService

    private var smsServiceIntent: Intent? = null

    private fun initSmsServiceIntent() {
        if (permissionManager == null) {
            permissionManager = PermissionManager(this)
            if (permissionManager!!.checkPermissionForSmsRead()) {
                smsServiceIntent = Intent(this, SmsService::class.java)
                startService(smsServiceIntent)
                bindSmsService()
            }
        }
    }

    private fun bindSmsService() {
        if (serviceConnection == null) {
            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                    val binder = iBinder as SmsService.ServiceBinder
                    smsService = binder.service
                    scheduleMessageFetch()
                }

                override fun onServiceDisconnected(componentName: ComponentName) {
                    serviceBounded = false
                }
            }

            bindService(smsServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun scheduleMessageFetch() {
        val t = Timer()
        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    smsService.readMessages()
                }
            }
        }, //Set how long before to start calling the TimerTask (in milliseconds)
                8000,
                //Set the amount of time between each execution (in milliseconds)
                160000)
    }

    private var serviceConnection: ServiceConnection? = null
    private var broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, receiverIntent: Intent?) {
            if (receiverIntent != null && receiverIntent.hasExtra(Constants.TRANSACTION_LIST)) {
                val transactions: Transactions = receiverIntent.getSerializableExtra(Constants.TRANSACTION_LIST) as Transactions
                if (transactions.transactions != null) {
                    all.clear()
                    all.addAll(transactions.transactions!!)
                    fetchBudget()
                }
            }
        }
    }
}
