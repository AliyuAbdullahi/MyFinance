package latproject.com.myfinance.views.homescreen.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import latproject.com.myfinance.R
import latproject.com.myfinance.core.services.PermissionManager
import latproject.com.myfinance.core.services.sms.SmsListener
import latproject.com.myfinance.core.services.sms.SmsReader
import latproject.com.myfinance.core.services.sms.SmsReceiver
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivityHomeBinding
import latproject.com.myfinance.views.homescreen.viewmodels.HomeActivityViewModel

class HomeActivity : CoreActivity(), SmsListener {
    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeActivityViewModel
    override fun onMessageReceived(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = HomeActivityViewModel(this)

        checkPermissionForSMS()
        fetchBudget()
    }

    private fun fetchBudget() {
        val budgets = viewModel.getBudgets()
        if (budgets == null || budgets.isEmpty()) {
            setUpProgress(0.0, 0.0)
        } else {
            val activeBudget = budgets.first { it.active }

            setUpProgress(activeBudget.amountSpent, activeBudget.amount)
        }
    }

    private fun setUpProgress(amountSpent: Double, amount: Double) {

        val valueInPercent = amountSpent / amount * 100

        binding.budgetProgressBar.backgroundColor = ContextCompat.getColor(this, R.color.budget_default)
        binding.budgetProgressBar.color = ContextCompat.getColor(this, R.color.budget_fine)

        binding.moneySpent.text = "$amountSpent"

        binding.baseAmount.text = "$amount"

        binding.budgetProgressBar.progressBarWidth = resources.getDimension(R.dimen.size_16dp)
        Handler().postDelayed({
           binding.budgetPercentUsed.visibility = View.VISIBLE
            if(amount == 0.0) {
                binding.budgetPercentUsed.text = "-"
            } else {
                binding.budgetPercentUsed.text = "$valueInPercent%"
            }
        }, 2500)

        binding.budgetProgressBar.setProgressWithAnimation(valueInPercent.toFloat(), 2500)
    }

    private fun checkPermissionForSMS() {
        val permissionManager = PermissionManager(this)

        if(!permissionManager.checkPermissionForSmsReceive()) {
            permissionManager.requestPermissionForSmsReceive()
        } else {
            initSms()
        }
    }

    fun initSms() {
        SmsReceiver.bindListener(this)

        val smsReader = SmsReader(this)
        smsReader.getMessage().forEach {
//            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
